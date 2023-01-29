package com.medicationtracker.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.medicationtracker.app.SaveAlarmData
import com.medicationtracker.app.TJNotifications
import io.karn.notify.Notify
import android.text.format.DateFormat
import com.medicationtracker.app.service.AlarmService
import com.medicationtracker.app.util.Constants
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)

        when (intent.action) {
            Constants.ACTION_SET_EXACT_ALARM -> {
                buildNotification(context, "Set Exact Time", convertDate(timeInMillis))
            }

            Constants.ACTION_SET_REPETITIVE_WEEKLY_ALARM -> {
                val cal = Calendar.getInstance().apply {
                    this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
                }
                AlarmService(context).setWeeklyAlarm(cal.timeInMillis)
                buildNotification(context, "Set Weekly Time", convertDate(timeInMillis))
            }

            Constants.ACTION_SET_REPETITIVE_DAILY_ALARM -> {
                val cal = Calendar.getInstance().apply {
                    this.timeInMillis = timeInMillis + TimeUnit.HOURS.toMillis(24)
                }
                AlarmService(context).setDailyAlarm(cal.timeInMillis)
                buildNotification(context, "Set Daily Time", convertDate(timeInMillis))
            }
        }
    }

    private fun buildNotification(context: Context, title: String, message: String){
        Notify
            .with(context)
            .content {
                this.title = title
                this.text = "It's time to take medicine"
            }

            .show()
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
}