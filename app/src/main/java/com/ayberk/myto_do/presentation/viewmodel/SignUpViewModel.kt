package com.ayberk.myto_do.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayberk.myto_do.presentation.models.User
import com.ayberk.myto_do.presentation.util.Resource
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {

    private val _register = MutableLiveData<Resource<User>>(Resource.Unspecified())
    val register: LiveData<Resource<User>> = _register
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _registrationSuccessful = MutableLiveData<Boolean>()
    val registrationSuccessful: LiveData<Boolean> = _registrationSuccessful

     fun createAccount(user: User, password: String) {
         auth.createUserWithEmailAndPassword(user.email, password)
             .addOnSuccessListener { authResult ->
                 authResult.user?.let {
                     _register.value = Resource.Success(user)
                     _registrationSuccessful.value = true
                 }
             }
             .addOnFailureListener {
                 _register.value = Resource.Error(it.cause.toString())
             }
     }
}