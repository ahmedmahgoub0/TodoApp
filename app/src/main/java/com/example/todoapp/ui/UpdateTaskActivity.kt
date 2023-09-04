package com.example.todoapp.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.todoapp.database.MyDatabase
import com.example.todoapp.database.model.Task
import com.example.todoapp.databinding.ActivityUpdateTaskBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date

class UpdateTaskActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityUpdateTaskBinding
    var task: Task? = null
    private var calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initParams()
        initViews()

    }

    private fun initParams() {
        task = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getParcelableExtra(Constants.TASK_KEY, Task::class.java)
        else
            intent.getParcelableExtra(Constants.TASK_KEY) as Task?
    }

    @SuppressLint("SimpleDateFormat")
    private fun initViews() {
        setSupportActionBar(viewBinding.updateToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        viewBinding.taskTitleTv.textAlignment = View.TEXT_ALIGNMENT_CENTER
        viewBinding.taskTitleTv.text = task?.title
        viewBinding.taskDescTv.text = task?.description
        //viewBinding.taskDescTv.text = task?.description

        val formatter = SimpleDateFormat("EEE, dd/MM/yyyy")
        var formattedDate = formatter.format(Date(task?.dateTime!!))
        viewBinding.taskDate.text = formattedDate

        viewBinding.taskDate.setOnClickListener {
            showDatePickerDialog()
        }

        viewBinding.updateTaskBtn.setOnClickListener{
            updateTaskInDatabase()
            viewBinding.taskTitleTv.text = task?.title
            viewBinding.taskDescTv.text = task?.description
            Snackbar.make(viewBinding.root, "Updated successfully", Snackbar.LENGTH_LONG).show()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun showDatePickerDialog() {
        val dialog = DatePickerDialog(this)
        dialog.setOnDateSetListener { view, year, month, day ->
            calendar.set(java.util.Calendar.YEAR,year)
            calendar.set(java.util.Calendar.MONTH,month)
            calendar.set(java.util.Calendar.DAY_OF_MONTH,day)
            // ignore time
            calendar.set(java.util.Calendar.HOUR, 0)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
            calendar.set(java.util.Calendar.MINUTE, 0)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)
            val formatter = SimpleDateFormat("EEE, dd/MM/yyyy")
            var formattedDate = formatter.format(Date(calendar.timeInMillis))
            viewBinding.taskDate.text = formattedDate
        }
        dialog.show()
    }

    private fun updateTaskInDatabase() {
        if(!viewBinding.taskTitle.text.toString().isNullOrBlank()){
            task?.title = viewBinding.taskTitle.text.toString()
        }
        if(!viewBinding.taskDesc.text.toString().isNullOrBlank()){
            task?.description = viewBinding.taskDesc.text.toString()
        }
        task?.dateTime = calendar.timeInMillis
        MyDatabase.getInstance(applicationContext)
            .tasksDao()
            .updateTask(task!!)
    }

}