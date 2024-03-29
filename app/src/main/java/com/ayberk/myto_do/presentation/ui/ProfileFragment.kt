package com.ayberk.myto_do.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ayberk.myto_do.R
import com.ayberk.myto_do.databinding.FragmentProfileBinding
import com.ayberk.myto_do.presentation.viewmodel.SignInViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imgBack.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
        firebaseUser?.let { user ->
            val userEmail = user.email
            binding.txtUserEmail.text = userEmail
        }
        binding.imgExit.setOnClickListener {
            firebaseAuth.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_signInFragment)
        }
    }
}