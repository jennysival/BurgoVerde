package com.jennysival.burgoverde.ui.baseAuth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigator
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigatorImpl
import com.jennysival.burgoverde.utils.helper.UserInputValidator
import com.jennysival.burgoverde.utils.showSnackBar
import com.jennysival.burgoverde.utils.viewstate.AuthViewState

abstract class BaseAuthFragment : Fragment() {
    protected lateinit var validator: UserInputValidator
    private lateinit var navigator: BurgoVerdeNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initValidator()
        initNavigator()
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
                is AuthViewState.Success -> navigator.navigate(
                    actionId = successActionId,
                    message = successMessage
                )
                is AuthViewState.Error -> showSnackBar(
                    messageRes = it.authError.messageRes,
                    view = rootView,
                    context = requireContext()
                )
            }
        }
    }
}
