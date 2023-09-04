package com.example.todoapp.ui.home.tabs.tasksList

import android.annotation.SuppressLint
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.model.Task
import com.example.todoapp.databinding.ItemTaskBinding
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Date

class TasksAdapter(var tasksList: MutableList<Task>?): RecyclerView.Adapter<TasksAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tasksList!!.get(position))
        if(onViewClickListener!=null){
            holder.itemBinding.dragItem.setOnClickListener  {
                onViewClickListener?.onItemClick(position, tasksList!![position])
            }
        }
        if(onItemDeleteListener!=null){
            holder.itemBinding.deleteView
                .setOnClickListener{
                    holder.itemBinding.swipeLayout.close(true)
                    onItemDeleteListener?.onItemClick(position, tasksList!![position])
                }
        }
        if(onDoneClickListener!=null){
            holder.itemBinding.doneButton
                .setOnClickListener{
                    onDoneClickListener?.onItemClick(position, tasksList!![position])
                }
        }
    }

    override fun getItemCount(): Int = tasksList?.size?:0

    class ViewHolder(val itemBinding:ItemTaskBinding): RecyclerView.ViewHolder(itemBinding.root){
        @SuppressLint("SimpleDateFormat", "ResourceAsColor", "UseCompatLoadingForDrawables")
        fun bind(task: Task){
            itemBinding.taskTitle.text = task.title
            val formatter = SimpleDateFormat("EEE, dd/MM/yyyy")
            val formattedDate = formatter.format(Date(task.dateTime!!))
            itemBinding.taskDate.text = formattedDate
            if(task.isDone == true){
                itemBinding.taskTitle.setTextColor(
                    itemBinding.taskTitle.resources.getColor(R.color.green, null)
                )
                itemBinding.view.setBackgroundColor(
                    itemBinding.view.resources.getColor(R.color.green, null)
                )
                itemBinding.doneButton.setBackgroundColor(
                    itemBinding.doneButton.resources.getColor(R.color.green, null)
                )
            } else{
                itemBinding.taskTitle.setTextColor(
                    itemBinding.taskTitle.resources.getColor(R.color.blue, null)
                )
                itemBinding.view.setBackgroundColor(
                    itemBinding.view.resources.getColor(R.color.blue, null)
                )
                itemBinding.doneButton.setBackgroundColor(
                    itemBinding.doneButton.resources.getColor(R.color.blue, null)
                )
            }
        }
    }

    var onViewClickListener: OnItemClickListener? = null
    var onDoneClickListener: OnItemClickListener? = null
    var onItemDeleteListener: OnItemClickListener? = null
    fun interface OnItemClickListener{
        fun onItemClick(position: Int, task: Task)
    }

    fun bindTasks( tasksList: MutableList<Task>) {
        this.tasksList = tasksList
        notifyDataSetChanged()
    }

    fun taskDeleted(task: Task) {
        val position = tasksList?.indexOf(task)
        tasksList?.remove(task)
        notifyDataSetChanged()
    }

    fun taskUpdated(task: Task) {
        val position = tasksList?.indexOf(task)
        notifyItemChanged(position!!)
    }

    fun updateList(todoListByDate: List<Task>) {
        tasksList = todoListByDate.toMutableList()
        notifyDataSetChanged()
    }

}