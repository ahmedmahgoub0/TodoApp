package com.example.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.todoapp.database.dao.TasksDao
import com.example.todoapp.database.model.Task

@Database(entities = [Task::class], version = 1)
abstract class MyDatabase: RoomDatabase() {

     abstract fun tasksDao(): TasksDao

     companion object{
         private var instance: MyDatabase? = null
         fun getInstance(context: Context): MyDatabase {
             if(instance == null) {
                 instance = Room.databaseBuilder(
                     context.applicationContext,
                     MyDatabase::class.java,
                     "taskDB")
                     .allowMainThreadQueries()
                     .fallbackToDestructiveMigration()
                     .build()
             }
             return instance!!
         }
     }

}