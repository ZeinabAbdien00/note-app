package com.iti.android_7.utils

import java.text.SimpleDateFormat
import java.util.*

class DateTimeManager {
    companion object {
        fun currentDateTime(): String {
            val calendar = Calendar.getInstance()
            val dtf = SimpleDateFormat("hh:mm a")
            return dtf.format(calendar.time)
        }
    }
}