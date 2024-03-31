package com.ayberk.myto_do.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayberk.myto_do.R
import com.ayberk.myto_do.databinding.FragmentProfileBinding
import com.ayberk.myto_do.presentation.adapter.TaskAdapter
import com.ayberk.myto_do.presentation.adapter.TaskProfileAdapter
import com.ayberk.myto_do.presentation.models.ToDoData
import com.ayberk.myto_do.presentation.viewmodel.HomeViewModel
import com.ayberk.myto_do.presentation.viewmodel.ProfileTaskViewModel
import com.ayberk.myto_do.presentation.viewmodel.SignInViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileTaskViewModel>()
    private lateinit var taskadapter: TaskProfileAdapter
    private lateinit var toDoItemList: MutableList<ToDoData>

    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.init()
        viewModel.fetchTasksFromFirebase()
        init()

        viewModel.toDoItemList.observe(viewLifecycleOwner) { newList ->
            toDoItemList.clear()
            toDoItemList.addAll(newList)
            taskadapter.notifyDataSetChanged()
        }

        binding.imgBack.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
        }
        firebaseUser?.let { user ->
            val userEmail = user.email
            binding.txtUserEmail.text = userEmail
        }
        binding.imgExit.setOnClickListener {
            firebaseAuth.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_signInFragment)
        }
    }
    private fun init() {
        val layoutManager = GridLayoutManager(context, 2)
        binding.rcyclerprofiletodo.layoutManager = layoutManager

        binding.rcyclerprofiletodo.setHasFixedSize(true)
        toDoItemList = mutableListOf()
        taskadapter = TaskProfileAdapter(toDoItemList)
        binding.rcyclerprofiletodo.adapter = taskadapter
    }
}