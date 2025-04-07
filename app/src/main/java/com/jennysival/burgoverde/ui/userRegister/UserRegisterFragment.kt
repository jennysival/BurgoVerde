package com.jennysival.burgoverde.ui.userRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.data.UserModel
import com.jennysival.burgoverde.databinding.FragmentUserRegisterBinding
import com.jennysival.burgoverde.factory.UserRegisterViewModelFactory
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigator
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigatorImpl
import com.jennysival.burgoverde.utils.UserInputValidator
import com.jennysival.burgoverde.utils.ViewState

class UserRegisterFragment : Fragment() {

    private lateinit var binding: FragmentUserRegisterBinding
    private lateinit var registerViewModel: AuthViewModel
    private lateinit var navigator: BurgoVerdeNavigator
    private val validator = UserInputValidator()

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
        initObservers()
        initNavigator()
        userRegisterClick()
    }

    private fun initViewModel() {
        registerViewModel = ViewModelProvider(
            this,
            UserRegisterViewModelFactory(requireActivity())
        )[AuthViewModel::class.java]
    }

    private fun initNavigator() {
        navigator = BurgoVerdeNavigatorImpl(this)
    }

    private fun initObservers() {
        registerViewModel.registerState.observe(this.viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {}
                is ViewState.Success -> navigator.navigateToHome(
                    actionId = R.id.action_userRegisterFragment_to_homeFragment,
                    message = "Registro feito com sucesso!"
                )

                is ViewState.Error -> showError()
            }
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "Erro ao registrar, tente novamente!", Toast.LENGTH_LONG)
            .show()
    }

    private fun userRegisterClick() {
        binding.registerBtn.setOnClickListener {

            val fields = listOf(
                Pair(binding.nameInputLayout, binding.nameInputLayout.editText?.text.toString()),
                Pair(binding.ageInputLayout, binding.ageInputLayout.editText?.text.toString()),
                Pair(binding.emailInputLayout, binding.emailInputLayout.editText?.text.toString()),
                Pair(
                    binding.passwordInputLayout,
                    binding.passwordInputLayout.editText?.text.toString()
                ),
                Pair(
                    binding.confirmPasswordInputLayout,
                    binding.confirmPasswordInputLayout.editText?.text.toString()
                ),
            )


            if (validator.validateFields(fields)) {
                registerViewModel.registerUser(
                    user =
                    UserModel(
                        name = fields[0].second,
                        age = fields[1].second,
                        email = fields[2].second,
                        password = fields[3].second,
                        confirmPassword = fields[4].second
                    )
                )
            }
        }
    }
}
