package com.ayberk.myto_do.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayberk.myto_do.databinding.FragmentHomeBinding
import com.ayberk.myto_do.presentation.adapter.TaskAdapter
import com.ayberk.myto_do.presentation.models.ToDoData
import com.ayberk.myto_do.presentation.viewmodel.HomeFragmentViewModel
import com.google.android.material.textfield.TextInputEditText

class HomeFragment : Fragment(), AddToDoPopupFragment.DialogNextBtnClickListener,
    TaskAdapter.TaskAdapterInterface {

    private var isBackPressed = false
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popupFragment: AddToDoPopupFragment
    private val viewModel by viewModels<HomeFragmentViewModel>()
    private lateinit var taskadapter: TaskAdapter
    private lateinit var toDoItemList: MutableList<ToDoData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isBackPressed = true
        }
        viewModel.init()
        init()
        registerEvents()
        viewModel.fetchTasksFromFirebase()

        viewModel.toDoItemList.observe(viewLifecycleOwner) { newList ->
            toDoItemList.clear()
            toDoItemList.addAll(newList)
            taskadapter.notifyDataSetChanged()
        }

    }

    private fun registerEvents(){
        binding.addTaskBtn.setOnClickListener {
            popupFragment = AddToDoPopupFragment()
            popupFragment.setListener(this)
            popupFragment.show(
                childFragmentManager,
                "AddTodoPopupFragment"
            )
        }
    }

    private fun init() {
        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        toDoItemList = mutableListOf()
        taskadapter = TaskAdapter(toDoItemList)
        taskadapter.setListener(this)
        binding.mainRecyclerView.adapter = taskadapter
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        viewModel.saveTask(todo, todoEt) { isSuccess ->
            requireActivity().runOnUiThread {
                if (isSuccess) {
                    Toast.makeText(requireContext(), "Task saved successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "An error occurred while saving the task", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDeleteItemClicked(toDoData: ToDoData, position: Int) {
            viewModel.deleteTask(toDoData,position){isSuccess->
                requireActivity().runOnUiThread {
                    if (isSuccess) {
                        Toast.makeText(requireContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "An error occurred while deleting the task", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onEditItemClicked(toDoData: ToDoData, position: Int) {
        TODO("Not yet implemented")
    }
}