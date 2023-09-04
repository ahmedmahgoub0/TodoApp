package com.example.todoapp.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.databinding.ActivityHomeBinding
import com.example.todoapp.ui.home.tabs.AddTaskFragment
import com.example.todoapp.ui.home.tabs.SettingsFragment
import com.example.todoapp.ui.home.tabs.tasksList.TaskListFragment
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityHomeBinding
    var taskListFragmentRef: TaskListFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.addTodoButton.setOnClickListener{
            showAddTaskBottomSheet()
        }

        viewBinding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.navigation_tasks_list->{
                    taskListFragmentRef = TaskListFragment()
                    showFragment(taskListFragmentRef!!)
                }
                R.id.navigation_settings->{
                    showFragment(SettingsFragment())
                }
            }
            return@setOnItemSelectedListener true
        }
        viewBinding.bottomNavigationView.selectedItemId = R.id.navigation_tasks_list

    }

    private fun showAddTaskBottomSheet() {
        val addTaskSheet = AddTaskFragment()
        addTaskSheet.onTaskAddedListener = AddTaskFragment.OnTaskAddedListener {
            Snackbar.make(viewBinding.root, "Added Successfully", Snackbar.LENGTH_LONG).show()
            taskListFragmentRef?.loadTasks()
        }
        addTaskSheet.show(supportFragmentManager, "")
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .commit()
    }
}