package com.jennysival.burgoverde.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.databinding.FragmentLoginBinding
import com.jennysival.burgoverde.factory.AuthViewModelFactory
import com.jennysival.burgoverde.ui.AuthViewModel
import com.jennysival.burgoverde.ui.baseAuth.BaseAuthFragment
import com.jennysival.burgoverde.utils.hideKeyboard

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
        observeAuthState(
            viewModel = viewModel,
            rootView = binding.root,
            successActionId = R.id.action_loginFragment_to_mainFragment
        )
        userLoginClick()
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
}
