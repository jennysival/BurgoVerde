package com.jennysival.burgoverde.ui.baseAuth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.jennysival.burgoverde.factory.PlantViewModelFactory
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigator
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigatorImpl
import com.jennysival.burgoverde.ui.plantRegister.PlantViewModel
import com.jennysival.burgoverde.utils.helper.UserInputValidator
import com.jennysival.burgoverde.utils.showSnackBar
import com.jennysival.burgoverde.utils.viewstate.AuthViewState
import kotlinx.coroutines.launch

abstract class BaseAuthFragment : Fragment() {
    protected lateinit var validator: UserInputValidator
    private lateinit var navigator: BurgoVerdeNavigator
    private lateinit var plantViewModel: PlantViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlantViewModel()
        initValidator()
        initNavigator()
    }

    private fun initPlantViewModel() {
        plantViewModel = ViewModelProvider(
            this, PlantViewModelFactory(requireActivity())
        )[PlantViewModel::class.java]
    }

    private fun initValidator() {
        validator = UserInputValidator(requireContext())
    }

    private fun initNavigator() {
        navigator = BurgoVerdeNavigatorImpl(this)
    }

    protected fun getTextFromInputLayout(inputLayout: TextInputLayout): String {
        return inputLayout.editText?.text.toString().trim()
    }

    protected fun observeAuthState(
        viewModel: AuthViewModel,
        rootView: View,
        successActionId: Int,
        successMessage: String? = null
    ) {
        viewModel.authState.observe(viewLifecycleOwner) {
            when (it) {
                is AuthViewState.Success -> {
                    syncPlantsAfterLogin()
                    navigator.navigate(
                        actionId = successActionId, message = successMessage
                    )
                }

                is AuthViewState.Error -> showSnackBar(
                    messageRes = it.authError.messageRes,
                    view = rootView,
                    context = requireContext()
                )
            }
        }
    }

    private fun syncPlantsAfterLogin() {
        lifecycleScope.launch {
            try {
                plantViewModel.syncPlants()
            } catch (e: Exception) {
                // do nothing
            }
        }
    }
}
