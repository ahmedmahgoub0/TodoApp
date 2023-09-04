package com.example.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.database.model.Task

@Dao
interface TasksDao {

    @Insert
    fun insertTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Delete
    fun deleteTask(task: Task)

    @Query("SELECT * FROM TASKS")
    fun getAllTasks(): List<Task>

    @Query("SELECT * FROM TASKS WHERE dateTime = :dateTime")
    fun getTasksByDate(dateTime: Long): List<Task>

}