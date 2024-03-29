package com.ayberk.myto_do.presentation.viewmodel

import android.provider.Settings.Global.getString
import androidx.lifecycle.ViewModel
import com.ayberk.myto_do.R
import com.ayberk.myto_do.presentation.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

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

    fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Giriş başarılı
                } else {
                    // Hata oluştu
                }
            }
    }
}