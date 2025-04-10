package com.jennysival.burgoverde.ui.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.databinding.FragmentProfileBinding
import com.jennysival.burgoverde.factory.ProfileViewModelFactory
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigator
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigatorImpl
import com.jennysival.burgoverde.utils.IMAGE_PICKER_PATH
import com.jennysival.burgoverde.utils.showSnackBar
import com.jennysival.burgoverde.utils.viewstate.ProfileViewState

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var navigator: BurgoVerdeNavigator

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                uploadProfileImage(it)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initNavigator()
        observeProfileImage()
        loadProfileImage()
        showUserName()
        changePhotoClick()
        logoutClick()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this, ProfileViewModelFactory(requireActivity())
        )[ProfileViewModel::class.java]
    }

    private fun initNavigator() {
        navigator = BurgoVerdeNavigatorImpl(this)
    }

    private fun showUserName() {
        binding.nameTv.text = viewModel.getUserName()
    }

    private fun logoutClick() {
        binding.logoutBtn.setOnClickListener {
            viewModel.logout()
            requireActivity()
                .findNavController(R.id.home_nav_fragment)
                .navigate(R.id.onboardingFragment)
        }
    }

    private fun changePhotoClick() {
        binding.changePicBtn.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch(IMAGE_PICKER_PATH)
    }

    private fun uploadProfileImage(uri: Uri) {
        viewModel.uploadProfileImage(uri)
    }

    private fun loadProfileImage() {
        viewModel.getProfileImage()
    }

    private fun observeProfileImage() {
        viewModel.imageState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileViewState.Success -> {
                    loadImage(uri = state.data)
                }
                is ProfileViewState.Error -> {
                    loadImage(null)
                }
            }
        }

        viewModel.uploadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileViewState.Success -> {
                    loadImage(uri = state.data)
                    showSnackBar(R.string.burgoverde_image_upload_success)
                }
                is ProfileViewState.Error -> {
                    showSnackBar(R.string.burgoverde_image_upload_error)
                }
            }
        }
    }

    private fun loadImage(uri: String?) {
        Glide.with(this)
            .load(uri.takeIf { !it.isNullOrBlank() })
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.photoIv)
    }

    private fun showSnackBar(messageRes: Int) {
        showSnackBar(
            messageRes = messageRes,
            view = binding.root,
            context = requireContext()
        )
    }
}
