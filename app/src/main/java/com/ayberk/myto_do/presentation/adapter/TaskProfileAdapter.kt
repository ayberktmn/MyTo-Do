package com.ayberk.myto_do.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayberk.myto_do.databinding.ProfiletodoItemBinding
import com.ayberk.myto_do.presentation.models.ToDoData

class TaskProfileAdapter(private val list:MutableList<ToDoData>) : RecyclerView.Adapter<TaskProfileAdapter.ToDoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskProfileAdapter.ToDoViewHolder {
        val binding = ProfiletodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                if (position >= itemCount - 2) {
                    binding.txttodoprofile.text = this.task
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return minOf(list.size, 2)
    }

    inner class ToDoViewHolder(val binding : ProfiletodoItemBinding) : RecyclerView.ViewHolder(binding.root)

}
