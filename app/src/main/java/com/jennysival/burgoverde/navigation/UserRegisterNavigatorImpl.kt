package com.jennysival.burgoverde.navigation

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.jennysival.burgoverde.R

class UserRegisterNavigatorImpl(private val fragment: Fragment) : BurgoVerdeNavigator {

    override fun navigateToHome() {
        NavHostFragment.findNavController(fragment)
            .navigate(R.id.action_userRegisterFragment_to_homeFragment)

        Toast.makeText(
            fragment.requireContext(),
            "Você foi registrado com sucesso!",
            Toast.LENGTH_LONG
        ).show()
    }

}