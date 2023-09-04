package com.example.todoapp.ui.home.tabs.tasksList

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.database.MyDatabase
import com.example.todoapp.database.model.Task
import com.example.todoapp.databinding.FragmentTaskListBinding
import com.example.todoapp.ui.Constants
import com.example.todoapp.ui.UpdateTaskActivity
import com.google.android.material.snackbar.Snackbar
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.WeekDayBinder
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskListFragment: Fragment() {

    private lateinit var viewBinding: FragmentTaskListBinding
    private val tasksAdapter = TasksAdapter(null)
    lateinit var calendarView: WeekCalendarViewHolder
    var selectedDate: LocalDate? = null
    var calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentTaskListBinding.inflate(
            inflater, container, false
        )
        return viewBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        loadTasks()
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    fun loadTasks() {
        context?.let{
            val tasksList = MyDatabase.getInstance(it).tasksDao().getAllTasks()
            tasksAdapter.bindTasks(tasksList.toMutableList())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initViews() {
        initTasksRecyclerView()
        bindWeekCalendarView()
    }

    private fun initTasksRecyclerView(){
        viewBinding.tasksRecyclerView.adapter = tasksAdapter
        tasksAdapter.onItemDeleteListener = TasksAdapter.OnItemClickListener{position, task->
            deleteTaskFromDatabase(task)
            tasksAdapter.taskDeleted(task)
            Snackbar.make(viewBinding.root, "Deleted successfully", Snackbar.LENGTH_LONG).show()
        }
        tasksAdapter.onDoneClickListener = TasksAdapter.OnItemClickListener {position, task->
            changeTaskStatue(task)
            tasksAdapter.taskUpdated(task)
        }
        tasksAdapter.onViewClickListener = TasksAdapter.OnItemClickListener { position, task->
            val intent = Intent(requireActivity(), UpdateTaskActivity::class.java)
            intent.putExtra(Constants.TASK_KEY, task)
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindWeekCalendarView(){
        viewBinding.weekCalendarView.dayBinder = object : WeekDayBinder<WeekCalendarViewHolder>{
            override fun create(view: View): WeekCalendarViewHolder {
                return WeekCalendarViewHolder(view)
            }
            override fun bind(container: WeekCalendarViewHolder, data: WeekDay) {
                container.dayOfMonth.text = data.date.dayOfMonth.toString()
                container.dayOfWeek.text = data.date.dayOfWeek
                    .getDisplayName(TextStyle.SHORT, Locale.getDefault())
                val currentSelected = selectedDate
                container.weekCalendarView.setOnClickListener{
                    if(currentSelected == data.date){
                        selectedDate = null
                        getTasksByDate(null)
                        viewBinding.weekCalendarView.notifyDateChanged(currentSelected)
                    } else{
                        selectedDate = data.date
                        viewBinding.weekCalendarView.notifyDateChanged(data.date)
                        if(currentSelected!=null){
                            viewBinding.weekCalendarView.notifyDateChanged(currentSelected)
                        }
                    }
                }
                if(selectedDate == data.date){
                    container.dayOfWeek.setTextColor(resources.getColor(R.color.blue, null))
                    container.dayOfMonth.setTextColor(resources.getColor(R.color.blue, null))

                    calendar.set(Calendar.DAY_OF_MONTH, selectedDate?.dayOfMonth!!)
                    calendar.set(Calendar.MONTH, selectedDate?.monthValue?.minus(1)!!)
                    calendar.set(Calendar.YEAR, selectedDate?.year!!)
                    calendar.set(Calendar.HOUR, 0)
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    getTasksByDate(calendar.timeInMillis)
                } else{
                    container.dayOfWeek.setTextColor(resources.getColor(R.color.black, null))
                    container.dayOfMonth.setTextColor(resources.getColor(R.color.black, null))
                }

            }
        }
        val currentDate = LocalDate.now()
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.minusMonths(100).atStartOfMonth() // Adjust as needed
        val endDate = currentMonth.plusMonths(100).atEndOfMonth()  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale()
        viewBinding.weekCalendarView.setup(startDate, endDate, firstDayOfWeek)
        viewBinding.weekCalendarView.scrollToWeek(currentDate)
    }

    private fun getTasksByDate(date: Long?) {
        val todoListByDate = if(date == null){
            MyDatabase.getInstance(requireContext())
                .tasksDao()
                .getAllTasks()
        }else{
            MyDatabase.getInstance(requireContext())
                .tasksDao()
                .getTasksByDate(date)
        }
        tasksAdapter.updateList(todoListByDate)
    }

    private fun changeTaskStatue(task: Task) {
        task.isDone = task.isDone != true
        Log.e("checked", task.isDone.toString())
        MyDatabase.getInstance(requireContext())
            .tasksDao()
            .updateTask(task)
    }

    private fun deleteTaskFromDatabase(task: Task) {
        MyDatabase.getInstance(requireContext())
            .tasksDao()
            .deleteTask(task)
    }


}