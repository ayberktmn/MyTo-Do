package com.ayberk.myto_do.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ayberk.myto_do.R
import com.ayberk.myto_do.databinding.FragmentSignInBinding
import com.ayberk.myto_do.presentation.models.User
import com.ayberk.myto_do.presentation.viewmodel.HomeViewModel
import com.ayberk.myto_do.presentation.viewmodel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class SignInFragment : Fragment() {

    private val viewModel by viewModels<SignInViewModel>()
    private lateinit var navController : NavController
    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!
    companion object {
        private const val RC_SIGN_IN = 9001
    }
    private var isBackPressed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater,container,false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isBackPressed = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        binding.gSignInBtn.setOnClickListener {
            binding.progressBarSignIn.visibility = View.VISIBLE
            signIn()
        }
        binding.textViewSignUp.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                binding.progressBarSignIn.visibility = View.VISIBLE
                login(email,pass)
            } else {
                Toast.makeText(context, "Empty fields are not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun login(email: String, password: String) {
        val user = User(email)
        viewModel.signinEmail(user, password) { isSuccess ->
            if (isSuccess) {
                // Giriş başarılı oldu, ilgili işlemleri yapabilirsiniz
                binding.progressBarSignIn.visibility = View.GONE
                navController.navigate(R.id.action_signInFragment_to_homeFragment)
                Toast.makeText(context, "Welcome My To-Do", Toast.LENGTH_SHORT).show()
            } else {
                // Giriş başarısız oldu, kullanıcıya bilgi verilebilir
                binding.progressBarSignIn.visibility = View.GONE
                Toast.makeText(context, "Login unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.signInWithGoogle(account.idToken!!)
                binding.progressBarSignIn.visibility = View.GONE
                navController.navigate(R.id.action_signInFragment_to_homeFragment)

            } catch (e: ApiException) {
                binding.progressBarSignIn.visibility = View.GONE
                Toast.makeText(requireContext(), "Google sign in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
    }
}