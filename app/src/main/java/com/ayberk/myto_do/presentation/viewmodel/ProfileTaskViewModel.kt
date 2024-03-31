package com.ayberk.myto_do.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayberk.myto_do.presentation.models.ToDoData
import com.ayberk.myto_do.presentation.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProfileTaskViewModel : ViewModel()  {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String

    private val _toDoItemList = MutableLiveData<List<ToDoData>>()
    val toDoItemList: LiveData<List<ToDoData>> = _toDoItemList

    fun init(){
        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser?.uid.toString()
        database = Firebase.database.reference.child("Tasks")
            .child(authId)
    }

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val list = mutableListOf<ToDoData>()
            for (taskSnapshot in snapshot.children) {
                val task = taskSnapshot.value as? String
                task?.let {
                    val todoData = ToDoData(id = taskSnapshot.key, task = it)
                    list.add(todoData)
                }
            }
            _toDoItemList.value = list
        }

        override fun onCancelled(error: DatabaseError) {
            val errorMessage = "Firebase Database Error: ${error.message}"
            val resource = Resource.Error(errorMessage)
            Log.e("Error Message", resource.toString())
        }
    }

    fun fetchTasksFromFirebase() {
        database.addValueEventListener(valueEventListener)
    }
}