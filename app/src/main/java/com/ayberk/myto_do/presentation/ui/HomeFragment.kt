package com.ayberk.myto_do.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ayberk.myto_do.R
import com.ayberk.myto_do.databinding.FragmentHomeBinding
import com.ayberk.myto_do.presentation.adapter.TaskAdapter
import com.ayberk.myto_do.presentation.models.ToDoData
import com.ayberk.myto_do.presentation.viewmodel.HomeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText

class HomeFragment : Fragment(), AddToDoPopupFragment.DialogNextBtnClickListener,
    TaskAdapter.TaskAdapterInterface {

    private var isBackPressed = false
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popupFragment: AddToDoPopupFragment
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var taskadapter: TaskAdapter
    private lateinit var toDoItemList: MutableList<ToDoData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        popupFragment = AddToDoPopupFragment()
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
        init(view)

        viewModel.fetchTasksFromFirebase()

        viewModel.toDoItemList.observe(viewLifecycleOwner) { newList ->
            toDoItemList.clear()
            toDoItemList.addAll(newList)
            taskadapter.notifyDataSetChanged()
        }

        binding.imgMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            requireActivity(), binding.drawerLayout, R.string.Home, R.string.Profile
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)

        actionBarDrawerToggle.setToolbarNavigationClickListener {
            navController.navigate(R.id.homeFragment)
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Home -> navController.navigate(R.id.homeFragment)
                R.id.Profile -> navController.navigate(R.id.profileFragment)
                R.id.addtodo -> registerEvents()
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        binding.addTaskBtn.setOnClickListener {
            registerEvents()
        }
    }
    private fun registerEvents(){
        if (popupFragment != null)
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
        popupFragment = AddToDoPopupFragment()
        popupFragment.setListener(this)
        popupFragment.show(
            childFragmentManager,
            AddToDoPopupFragment.TAG
        )
    }

    private fun init(view: View) {
        binding.mainRecyclerView.setHasFixedSize(true)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        toDoItemList = mutableListOf()
        taskadapter = TaskAdapter(toDoItemList)
        taskadapter.setListener(this)
        binding.mainRecyclerView.adapter = taskadapter
        navController = Navigation.findNavController(view)
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

    override fun onUpdateTask(toDoData: ToDoData, todoEt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[toDoData.id.toString()] = toDoData.task.toString()

        viewModel.updateTask(map) { isSuccess ->
            requireActivity().runOnUiThread {
                if (isSuccess) {
                    Toast.makeText(requireContext(), "Task update successfully", Toast.LENGTH_SHORT).show()
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
        if(popupFragment != null){
            childFragmentManager.beginTransaction().remove(popupFragment).commit()

            popupFragment = AddToDoPopupFragment.newInstance(toDoData.id.toString(), toDoData.task.toString())
            popupFragment.setListener(this)
            popupFragment.show(childFragmentManager,AddToDoPopupFragment.TAG)
        }
    }
}