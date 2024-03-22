package com.ayberk.myto_do.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayberk.myto_do.presentation.models.User
import com.ayberk.myto_do.presentation.util.Resource
import com.google.firebase.auth.FirebaseAuth


class LoginViewModel : ViewModel() {

    private val _register = MutableLiveData<Resource<User>>(Resource.Unspecified())
    val register: LiveData<Resource<User>> = _register
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _existingAccountFound = MutableLiveData<Boolean>()
    val existingAccountFound: LiveData<Boolean> = _existingAccountFound

    private val _registrationSuccessful = MutableLiveData<Boolean>()
    val registrationSuccessful: LiveData<Boolean> = _registrationSuccessful

    fun createEmailandPassword(user: User, password: String){
        auth.createUserWithEmailAndPassword(user.email,password)
            .addOnSuccessListener {
                it.user?.let {
                    _register.value = Resource.Success(user)
                    _registrationSuccessful.value = true
                }
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.cause.toString())
            }
    }
}