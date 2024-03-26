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
import com.ayberk.myto_do.R
import com.ayberk.myto_do.databinding.FragmentHomeBinding
import com.ayberk.myto_do.presentation.viewmodel.HomeFragmentViewModel
import com.ayberk.myto_do.presentation.viewmodel.SignInViewModel
import com.google.android.material.textfield.TextInputEditText

class HomeFragment : Fragment(), AddToDoPopupFragment.DialogNextBtnClickListener {

    private var isBackPressed = false
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var popupFragment: AddToDoPopupFragment
    private val viewModel by viewModels<HomeFragmentViewModel>()
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
        init(view)
        registerEvents()
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

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
        viewModel.saveTask(todo, todoEt) { isSuccess ->
            requireActivity().runOnUiThread {
                if (isSuccess) {
                    Toast.makeText(requireContext(), "Görev başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Görev kaydedilirken bir hata oluştu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}