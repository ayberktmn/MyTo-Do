package com.ayberk.myto_do.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ayberk.myto_do.R
import com.ayberk.myto_do.databinding.FragmentSignUpBinding
import com.ayberk.myto_do.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth



class SplashFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding : FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater,container,false)

        Handler(Looper.getMainLooper()).postDelayed({

            mAuth = FirebaseAuth.getInstance()


                findNavController().navigate(R.id.action_splashFragment_to_signUpFragment)

        },3000)

        return binding.root

    }
}