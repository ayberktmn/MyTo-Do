package com.ayberk.myto_do.presentation.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayberk.myto_do.presentation.models.User
import com.ayberk.myto_do.presentation.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginViewModel : ViewModel() {

    private val _register = MutableLiveData<Resource<User>>(Resource.Unspecified())
    val register: LiveData<Resource<User>> = _register
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    private val _existingAccountFound = MutableLiveData<Boolean>()
    val existingAccountFound: LiveData<Boolean> = _existingAccountFound

    private val _registrationSuccessful = MutableLiveData<Boolean>()
    val registrationSuccessful: LiveData<Boolean> = _registrationSuccessful

    fun createEmailandPassword(user: User, password: String) {
        auth.fetchSignInMethodsForEmail(user.email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods ?: emptyList<String>()
                    if (signInMethods.isEmpty()) {
                        // E-posta adresiyle ilişkilendirilmiş bir hesap var, kullanıcıyı uyar
                        _existingAccountFound.value = true
                        _register.value = Resource.Fail("An account already exists with this email.")
                    } else {
                        // E-posta adresiyle ilişkilendirilmiş bir hesap yok, kullanıcıyı oluştur
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
                } else {
                    // Hata durumunda kullanıcıya bir uyarı göster
                    _register.value = Resource.Error("An error occurred while checking for existing accounts.")
                }
            }
    }
}