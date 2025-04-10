package com.jennysival.burgoverde.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.databinding.FragmentOnboardingBinding
import com.jennysival.burgoverde.factory.AuthViewModelFactory
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigator
import com.jennysival.burgoverde.navigation.BurgoVerdeNavigatorImpl
import com.jennysival.burgoverde.ui.baseAuth.AuthViewModel

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var navigator: BurgoVerdeNavigator
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initNavigator()
        setupUserOnboardingNavigation()

    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this, AuthViewModelFactory(requireActivity())
        )[AuthViewModel::class.java]
    }

    private fun initNavigator() {
        navigator = BurgoVerdeNavigatorImpl(this)
    }

    private fun setupUserOnboardingNavigation() {
        if (viewModel.isUserLoggedIn()) {
            navigator.navigate(
                actionId = R.id.action_onboardingFragment_to_mainFragment,
                message = null
            )
        } else {
            userRegisterClick()
            userLoginClick()
        }
    }

    private fun userRegisterClick() {
        binding.registerBtn.setOnClickListener {
            navigator.navigate(
                actionId = R.id.action_onboardingFragment_to_userRegisterFragment,
                message = null
            )
        }
    }

    private fun userLoginClick() {
        binding.loginBtn.setOnClickListener {
            navigator.navigate(
                actionId = R.id.action_onboardingFragment_to_loginFragment,
                null
            )
        }
    }
}
