package com.ayberk.myto_do.presentation.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.coroutineContext

class HomeFragmentViewModel : ViewModel() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String

    fun init(){
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = Firebase.database.reference.child("Tasks")
            .child(authId)
    }

    fun saveTask(todo: String, todoEt: TextInputEditText, callback: (Boolean) -> Unit) {
        val taskReference = database.push()
        val taskId = taskReference.key
        taskId?.let { taskId ->
            database.child(taskId).setValue(todo).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }
        }
    }
}