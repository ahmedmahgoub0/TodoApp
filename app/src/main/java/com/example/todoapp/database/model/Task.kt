package com.example.todoapp.database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Parcelize
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int? = null,
    @ColumnInfo
    var title: String? ="",
    @ColumnInfo
    var description: String? = "",
    @ColumnInfo
    var dateTime: Long? = null,
    @ColumnInfo
    var isDone: Boolean? = false
): Parcelable