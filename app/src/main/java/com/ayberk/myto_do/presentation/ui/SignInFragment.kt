package com.ayberk.myto_do.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ayberk.myto_do.R
import com.ayberk.myto_do.databinding.FragmentSignInBinding
import com.ayberk.myto_do.presentation.models.User
import com.ayberk.myto_do.presentation.viewmodel.SignInViewModel

class SignInFragment : Fragment() {

    private val viewModel by viewModels<SignInViewModel>()
    private lateinit var navController : NavController
    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        binding.textViewSignUp.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                login(email,pass)
            } else {
                Toast.makeText(context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun login(email: String, password: String) {
        val user = User(email)
        viewModel.signinEmail(user, password) { isSuccess ->
            if (isSuccess) {
                // Giriş başarılı oldu, ilgili işlemleri yapabilirsiniz
                navController.navigate(R.id.action_signInFragment_to_homeFragment)
                Toast.makeText(context, "Welcome My To-Do", Toast.LENGTH_SHORT).show()
            } else {
                // Giriş başarısız oldu, kullanıcıya bilgi verilebilir
                Toast.makeText(context, "Login unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
    }
}