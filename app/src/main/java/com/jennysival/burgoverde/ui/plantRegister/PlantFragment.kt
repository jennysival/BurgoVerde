package com.jennysival.burgoverde.ui.plantRegister

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.data.PlantModel
import com.jennysival.burgoverde.databinding.DialogPlantDetailsBinding
import com.jennysival.burgoverde.databinding.FragmentPlantBinding
import com.jennysival.burgoverde.factory.PlantViewModelFactory
import com.jennysival.burgoverde.utils.DEFAULT_USER_NAME
import com.jennysival.burgoverde.utils.helper.ImagePickerHelper
import com.jennysival.burgoverde.utils.helper.SharedPreferencesHelper
import com.jennysival.burgoverde.utils.helper.UserInputValidator
import com.jennysival.burgoverde.utils.showToast
import com.jennysival.burgoverde.utils.viewstate.PlantViewState
import java.util.UUID

class PlantFragment : Fragment() {
    private lateinit var binding: FragmentPlantBinding
    private lateinit var viewModel: PlantViewModel
    private lateinit var validator: UserInputValidator
    private lateinit var sharedPrefs: SharedPreferencesHelper

    private var currentPlantName: String = ""

    private val plantsAdapter: PlantsAdapter by lazy {
        PlantsAdapter(this::onPlantClick)
    }

    private lateinit var imagePicker: ImagePickerHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validator = UserInputValidator(requireContext())
        sharedPrefs = SharedPreferencesHelper(requireContext())
        imagePicker = ImagePickerHelper(this) { uri ->
            getPlantNameAndUpload(uri, plantName = currentPlantName)
        }

        initViewModel()
        initObserver()
        setUpRecyclerView()
        onAddPlantClick()

        viewModel.getPlants()
        viewModel.syncPlants()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this, PlantViewModelFactory(requireActivity())
        )[PlantViewModel::class.java]
    }

    private fun initObserver() {
        observeLoading()
        observeUpload()
        observePlantList()
    }

    private fun observeLoading() {
        viewModel.loadingState.observe(this.viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.uploadLoading.visibility = View.VISIBLE
                binding.addPlantButtonView.isEnabled = false
            } else {
                binding.uploadLoading.visibility = View.GONE
                binding.addPlantButtonView.isEnabled = true
            }
        }
    }

    private fun observeUpload() {
        viewModel.uploadState.observe(this.viewLifecycleOwner) {
            when (it) {
                is PlantViewState.Success -> {
                    if (it.data == Unit) {
                        showToast(
                            messageRes = R.string.burgoverde_image_upload_success,
                            context = requireContext()
                        )
                        viewModel.getPlants()
                        binding.plantInputLayout.editText?.text?.clear()
                        viewModel.clearUploadState()
                    }
                }

                is PlantViewState.Error -> {
                    showToast(
                        messageRes = R.string.burgoverde_image_upload_error,
                        context = requireContext()
                    )
                    viewModel.clearUploadState()
                }

                is PlantViewState.None -> {
                    //do nothing
                }
            }
        }
    }

    private fun observePlantList() {
        viewModel.plantListState.observe(this.viewLifecycleOwner) {
            when (it) {
                is PlantViewState.Success -> {
                    plantsAdapter.updatePlants(it.data)
                    binding.emptyPlantsTv.visibility = View.GONE
                    binding.rvPlants.visibility = View.VISIBLE
                    binding.rvPlants.scrollToPosition(0)
                    viewModel.clearListState()
                }

                is PlantViewState.Error -> {
                    binding.emptyPlantsTv.visibility = View.VISIBLE
                    binding.rvPlants.visibility = View.GONE
                    viewModel.clearListState()
                }

                is PlantViewState.None -> {
                    //do nothing
                }

            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvPlants.adapter = plantsAdapter
        binding.rvPlants.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun onPlantClick(clickedPlant: PlantModel) {
        showPlantDetailDialog(plantModel = clickedPlant)
    }

    private fun onAddPlantClick() {
        binding.addPlantButtonView.setOnClickListener {
            val plantName = binding.plantInputLayout.editText?.text.toString().trim()
            val isValid = validator.validateFields(
                fields = listOf(binding.plantInputLayout to plantName)
            )

            if (isValid) {
                currentPlantName = plantName
                imagePicker.pickImage()
            }
        }
    }

    private fun getPlantNameAndUpload(uri: Uri?, plantName: String) {
        uri?.let { imageUri ->
            uploadPlant(imageUri, plantName)
        } ?: run {
            showToast(
                messageRes = R.string.burgoverde_image_upload_error, context = requireContext()
            )
        }
    }

    private fun uploadPlant(uri: Uri, plantName: String) {
        val newPlant = PlantModel(
            id = UUID.randomUUID().toString(),
            name = plantName,
            firebaseUrl = "",
            localUri = uri.toString(),
            author = sharedPrefs.getUserName() ?: DEFAULT_USER_NAME
        )
        viewModel.savePlant(context = requireContext(), plant = newPlant, imageUri = uri)
    }

    private fun showPlantDetailDialog(plantModel: PlantModel) {
        val dialogBinding = DialogPlantDetailsBinding.inflate(LayoutInflater.from(requireContext()))

        val alertDialog = AlertDialog.Builder(requireContext()).setView(dialogBinding.root)
            .setNegativeButton(getString(R.string.bugoverde_close_button_text)) { dialog, _ -> dialog.dismiss() }
            .create()

        alertDialog.setOnShowListener {

            Glide.with(dialogBinding.root)
                .load(plantModel.localUri.takeIf { it.isNotBlank() } ?: plantModel.firebaseUrl)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).error(R.drawable.burgo_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(dialogBinding.plantImageView)

            dialogBinding.plantNameTextView.text = plantModel.name
            dialogBinding.plantImageView.contentDescription = plantModel.name

            dialogBinding.deleteButton.setOnClickListener {
                alertDialog.dismiss()
                showDeleteConfirmationDialog(plant = plantModel)
            }
        }

        alertDialog.show()
    }

    private fun showDeleteConfirmationDialog(plant: PlantModel) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.burgoverde_wish_to_delete))
            .setMessage(getString(R.string.burgoverde_plant_removal_message, plant.name))
            .setPositiveButton(getString(R.string.bugoverde_delete_button_text)) { _, _ ->
                viewModel.deletePlant(plant)
            }.setNegativeButton(getString(R.string.bugoverde_dismiss_button_text), null).show()
    }

}
