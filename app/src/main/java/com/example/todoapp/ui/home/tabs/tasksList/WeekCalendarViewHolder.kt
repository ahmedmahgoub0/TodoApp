package com.example.todoapp.ui.home.tabs.tasksList

import android.view.View
import android.widget.TextView
import com.example.todoapp.R
import com.kizitonwose.calendar.view.ViewContainer

class WeekCalendarViewHolder(val weekCalendarView: View)
    : ViewContainer(weekCalendarView) {
    var dayOfMonth: TextView = weekCalendarView.findViewById(R.id.day_number)
    var dayOfWeek: TextView = weekCalendarView.findViewById(R.id.day_name)
}