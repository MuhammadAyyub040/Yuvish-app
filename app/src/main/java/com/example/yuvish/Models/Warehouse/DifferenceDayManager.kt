package com.example.yuvish.Models.Warehouse

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.example.yuvish.R
import java.util.*
import java.util.concurrent.TimeUnit

class DifferenceDayManager(private val submittedDate: String, private val context: Context) {

    val differanceDay: Long by lazy {
        getDifferenceDay()
    }

    fun getColor(): Int {
        return when{
            differanceDay > 0L -> {
                ContextCompat.getColor(context, R.color.green)
            }
            differanceDay == 0L -> {
                ContextCompat.getColor(context, R.color.yellow)
            }
            differanceDay < 0L -> {
                ContextCompat.getColor(context, R.color.red)
            }
            else -> {
                ContextCompat.getColor(context, R.color.red)
            }
        }
    }

    fun getResource(): Drawable {
        return when{
            differanceDay > 0L -> {
                ContextCompat.getDrawable(context, R.drawable.custom_input_4)!!
            }
            differanceDay == 0L -> {
                ContextCompat.getDrawable(context, R.drawable.ticket_background_yellow)!!
            }
            differanceDay < 0L -> {
                ContextCompat.getDrawable(context, R.drawable.custom_input_3)!!
            }
            else -> {
                ContextCompat.getDrawable(context, R.drawable.custom_input_3)!!
            }
        }
    }

    private fun getDifferenceDay(): Long{
        val dateArray = submittedDate.split("-")

        val calendarLast = Calendar.getInstance()
        calendarLast.set(Calendar.MILLISECOND, 0)
        calendarLast.set(Calendar.SECOND, 0)
        calendarLast.set(Calendar.MINUTE, 0)
        calendarLast.set(Calendar.HOUR_OF_DAY, 0)
        calendarLast.set(Calendar.DAY_OF_MONTH,dateArray[2].toInt())
        calendarLast.set(Calendar.MONTH,dateArray[1].toInt()-1)
        calendarLast.set(Calendar.YEAR,dateArray[0].toInt())

        val difference = calendarLast.timeInMillis - Date().time
        return TimeUnit.MILLISECONDS.toDays(difference)
    }

}