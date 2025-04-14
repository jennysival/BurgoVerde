package com.jennysival.burgoverde.navigation

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

class BurgoVerdeNavigatorImpl(private val fragment: Fragment) : BurgoVerdeNavigator {

    override fun navigate(actionId: Int, message: String?) {
        NavHostFragment.findNavController(fragment)
            .navigate(actionId)

        message?.let {
            Toast.makeText(
                fragment.requireContext(),
                it,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}
