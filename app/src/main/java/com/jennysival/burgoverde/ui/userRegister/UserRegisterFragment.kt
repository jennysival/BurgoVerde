package com.jennysival.burgoverde.ui.userRegister

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.jennysival.burgoverde.R
import com.jennysival.burgoverde.databinding.FragmentUserRegisterBinding

class UserRegisterFragment : Fragment() {

    private lateinit var binding: FragmentUserRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userRegisterClick()
    }

    private fun userRegisterClick() {
        binding.registerBtn.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_userRegisterFragment_to_homeFragment)

            Toast.makeText(
                this.activity,
                "Você foi registrado com sucesso!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}