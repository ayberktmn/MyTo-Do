package com.ayberk.myto_do.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ayberk.myto_do.presentation.models.ToDoData
import com.ayberk.myto_do.presentation.util.Resource
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragmentViewModel : ViewModel() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String

    private val _toDoItemList = MutableLiveData<List<ToDoData>>()
    val toDoItemList: LiveData<List<ToDoData>> = _toDoItemList

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

    override fun onCleared() {
        super.onCleared()
        database.removeEventListener(valueEventListener)
    }
    fun deleteTask(toDoData: ToDoData, position: Int, callback: (Boolean) -> Unit){
        database.child(toDoData.id.toString()).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                callback(true)
            }else{
                callback(false)
            }
        }
    }
}