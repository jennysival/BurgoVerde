package com.jennysival.burgoverde.ui.userRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.data.UserModel
import com.jennysival.burgoverde.databinding.FragmentUserRegisterBinding
import com.jennysival.burgoverde.factory.AuthViewModelFactory
import com.jennysival.burgoverde.ui.AuthViewModel
import com.jennysival.burgoverde.ui.baseAuth.BaseAuthFragment
import com.jennysival.burgoverde.utils.hideKeyboard

class UserRegisterFragment : BaseAuthFragment() {

    private lateinit var binding: FragmentUserRegisterBinding
    private lateinit var registerViewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        observeAuthState(
            viewModel = registerViewModel,
            rootView = binding.root,
            successAction = { R.id.action_userRegisterFragment_to_homeFragment },
            successMessage = getString(R.string.burgoverde_success_register_text)
        )
        userRegisterClick()
    }

    private fun initViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            AuthViewModelFactory(requireActivity())
        )[AuthViewModel::class.java]
    }

    private fun userRegisterClick() {
        binding.registerBtn.setOnClickListener {
            view?.hideKeyboard()
            handleUserRegistration()
        }
    }

    private fun handleUserRegistration() {
        val userModel = getUserModelFromFields()
        if (validateFields(userModel)) {
            registerViewModel.registerUser(userModel)
        }
    }

    private fun getUserModelFromFields(): UserModel {
        val name = getTextFromInputLayout(binding.nameInputLayout)
        val age = getTextFromInputLayout(binding.ageInputLayout)
        val email = getTextFromInputLayout(binding.emailInputLayout)
        val password = getTextFromInputLayout(binding.passwordInputLayout)
        val confirmPassword = getTextFromInputLayout(binding.confirmPasswordInputLayout)

        return UserModel(name, age, email, password, confirmPassword)
    }

    private fun validateFields(userModel: UserModel): Boolean {
        val fields = listOf(
            Pair(binding.nameInputLayout, userModel.name),
            Pair(binding.ageInputLayout, userModel.age),
            Pair(binding.emailInputLayout, userModel.email),
            Pair(binding.passwordInputLayout, userModel.password),
            Pair(binding.confirmPasswordInputLayout, userModel.confirmPassword)
        )

        val emailValidation = listOf(
            Pair(binding.emailInputLayout) { emailText: String -> validator.isValidEmail(emailText) }
        )

        return validator.validateFields(
            fields = fields,
            passwordLayout = binding.passwordInputLayout,
            confirmPasswordLayout = binding.confirmPasswordInputLayout,
            validations = emailValidation
        )
    }
}
