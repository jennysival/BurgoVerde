package com.jennysival.burgoverde.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.databinding.DialogResetPasswordBinding
import com.jennysival.burgoverde.databinding.FragmentLoginBinding
import com.jennysival.burgoverde.factory.AuthViewModelFactory
import com.jennysival.burgoverde.ui.baseAuth.AuthViewModel
import com.jennysival.burgoverde.ui.baseAuth.BaseAuthFragment
import com.jennysival.burgoverde.utils.hideKeyboard
import com.jennysival.burgoverde.utils.showSnackBar
import com.jennysival.burgoverde.utils.viewstate.AuthViewState

class LoginFragment : BaseAuthFragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        observeResetPasswordState()
        observeAuthState(
            viewModel = viewModel,
            rootView = binding.root,
            successActionId = R.id.action_loginFragment_to_mainFragment
        )
        userLoginClick()
        resetPasswordClick()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this, AuthViewModelFactory(requireActivity())
        )[AuthViewModel::class.java]
    }

    private fun userLoginClick() {
        binding.loginBtn.setOnClickListener {
            view?.hideKeyboard()
            handleUserLogin()
        }
    }

    private fun resetPasswordClick() {
        binding.resetPasswordBtn.setOnClickListener {
            showForgotPasswordDialog()
        }
    }

    private fun showForgotPasswordDialog() {
        val dialogBinding =
            DialogResetPasswordBinding.inflate(LayoutInflater.from(requireContext()))

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton(getString(R.string.bugoverde_send_button_text), null)
            .setNegativeButton(getString(R.string.bugoverde_dismiss_button_text)) { dialog, _ -> dialog.dismiss() }
            .create()

        alertDialog.setOnShowListener {
            val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val email = getTextFromInputLayout(dialogBinding.emailInputLayout)

                if (email.isEmpty()) {
                    dialogBinding.emailInputLayout.error =
                        getString(R.string.burgoverde_error_required_field)
                } else if (!validator.isValidEmail(email)) {
                    dialogBinding.emailInputLayout.error =
                        getString(R.string.burgoverde_invalid_email)
                } else {
                    dialogBinding.emailInputLayout.error = null
                    viewModel.resetPassword(email)
                    alertDialog.dismiss()
                }
            }
        }

        alertDialog.show()
    }


    private fun handleUserLogin() {
        val email = getTextFromInputLayout(binding.emailInputLayout)
        val password = getTextFromInputLayout(binding.passwordInputLayout)
        if (validateFields(email = email, password = password)) {
            viewModel.login(email = email, password = password)
        }
    }

    private fun validateFields(email: String, password: String): Boolean {
        val fields = listOf(
            Pair(binding.emailInputLayout, email),
            Pair(binding.passwordInputLayout, password)
        )

        val emailValidation = listOf(
            Pair(binding.emailInputLayout) { emailText: String -> validator.isValidEmail(emailText) }
        )

        return validator.validateFields(
            fields = fields,
            validations = emailValidation
        )
    }

    private fun observeResetPasswordState() {
        viewModel.resetPasswordState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewState.Success -> showSnackBar(
                    messageRes = R.string.burgoverde_email_sent_text,
                    view = binding.root,
                    context = requireContext()
                )

                is AuthViewState.Error -> showSnackBar(
                    messageRes = R.string.burgoverde_ops_text,
                    view = binding.root,
                    context = requireContext()
                )
            }
        }
    }
}
