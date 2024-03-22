package com.ayberk.myto_do.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.ayberk.myto_do.presentation.models.User
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signinEmail(user: User, password: String, onResult: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(user.email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Giriş başarılı oldu
                onResult(true)
            } else {
                // Giriş başarısız oldu
                onResult(false)
            }
        }
    }
}