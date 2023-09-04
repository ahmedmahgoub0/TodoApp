package com.example.todoapp.ui.home.tabs

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp.database.MyDatabase
import com.example.todoapp.database.model.Task
import com.example.todoapp.databinding.FragmentAddTaskBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class AddTaskFragment: BottomSheetDialogFragment() {

    lateinit var viewBinding: FragmentAddTaskBinding
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        calendar.set(Calendar.HOUR, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        viewBinding.timePicker.text =
            "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH)+1}/${calendar.get(Calendar.YEAR)}"
        viewBinding.doneFab.setOnClickListener{
            createTask()
        }
        viewBinding.timePicker.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val dialog = DatePickerDialog(requireActivity())
        dialog.setOnDateSetListener{
            datePicker, year, month, day ->
            viewBinding.timePicker.text = "$day/${month+1}/$year"
            calendar.set(Calendar.YEAR,year)
            calendar.set(Calendar.MONTH,month)
            calendar.set(Calendar.DAY_OF_MONTH,day)
            // ignore time
            calendar.set(Calendar.HOUR, 0)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }
        dialog.show()
    }

    private fun valid(): Boolean {
        var isValid = true
        if(viewBinding.taskTitle.text.toString().isNullOrBlank()) {
            viewBinding.taskTitleContainer.error = "Please enter title"
            isValid = false
        }
        else
            viewBinding.taskTitle.error = null
        return isValid
    }

    private fun createTask() {
        if(!valid())
            return
        val task = Task(
            title = viewBinding.taskTitle.text.toString(),
            description = viewBinding.taskDescription.text.toString(),
            dateTime = calendar.timeInMillis
        )
        MyDatabase.getInstance(requireContext())
            .tasksDao()
            .insertTask(task)
        onTaskAddedListener?.onTaskAdded()
        dismiss()
    }

    var onTaskAddedListener: OnTaskAddedListener? = null
    fun interface OnTaskAddedListener{
        fun onTaskAdded()
    }


}