package com.ayberk.myto_do.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayberk.myto_do.presentation.models.User
import com.ayberk.myto_do.presentation.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignUpViewModel : ViewModel() {

    private val _register = MutableLiveData<Resource<User>>(Resource.Unspecified())
    val register: LiveData<Resource<User>> = _register
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registrationSuccessful = MutableLiveData<Boolean>()
    val registrationSuccessful: LiveData<Boolean> = _registrationSuccessful

    private val _showEmailExistsWarning = MutableLiveData<Boolean>()
    val showEmailExistsWarning: LiveData<Boolean> = _showEmailExistsWarning

    fun createAccount(user: User, password: String) {
        auth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { authResult ->
                authResult.user?.let {
                    _register.value = Resource.Success(user)
                    _registrationSuccessful.value = true
                }
            }
            .addOnFailureListener { exception ->
                if (exception is FirebaseAuthUserCollisionException) {
                    _showEmailExistsWarning.value = true
                } else {
                    _register.value = Resource.Error(exception.message ?: "An error occurred")
                }
            }
    }
}