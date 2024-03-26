package com.ayberk.myto_do.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ayberk.myto_do.databinding.EachTodoItemBinding
import com.ayberk.myto_do.presentation.models.ToDoData

class TaskAdapter(private val list:MutableList<ToDoData>) : RecyclerView.Adapter<TaskAdapter.ToDoViewHolder>() {

    private var listener : TaskAdapterInterface? = null
    fun setListener(listener : TaskAdapterInterface){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface TaskAdapterInterface{
        fun onDeleteItemClicked(toDoData: ToDoData , position : Int)
        fun onEditItemClicked(toDoData: ToDoData , position: Int)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text = this.task

                binding.deleteTask.setOnClickListener {
                    listener?.onDeleteItemClicked(this,position)
                }

                binding.editTask.setOnClickListener {
                    listener?.onEditItemClicked(this,position)
                }
            }
        }
    }

    inner class ToDoViewHolder(val binding : EachTodoItemBinding) : RecyclerView.ViewHolder(binding.root)

}