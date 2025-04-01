package com.jennysival.burgoverde.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRegisterClick()
        userLoginClick()
    }

    private fun userRegisterClick() {
        binding.registerBtn.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_onboardingFragment_to_userRegisterFragment)
        }
    }

    private fun userLoginClick() {
        binding.loginBtn.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_onboardingFragment_to_loginFragment)
        }
    }
}