package com.medicationtracker.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.util.Log
import com.medicationtracker.app.service.AlarmService
import com.medicationtracker.app.util.Constants
import io.karn.notify.Notify
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {


        var medname = intent.getStringExtra("message").toString()


            val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)



            when (intent.action) {
                Constants.ACTION_SET_EXACT_ALARM -> {
                    buildNotification(context, "Set Exact Time")
                }

                Constants.ACTION_SET_REPETITIVE_WEEKLY_ALARM -> {
                    val cal = Calendar.getInstance().apply {
                        this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
                    }
                    AlarmService(context).setWeeklyAlarm(cal.timeInMillis, medname)
                    buildNotification(context, medname)
                }

                Constants.ACTION_SET_REPETITIVE_DAILY_ALARM -> {
                    val cal = Calendar.getInstance().apply {
                        this.timeInMillis = timeInMillis + TimeUnit.HOURS.toMillis(24)
                    }
                    AlarmService(context).setDailyAlarm(cal.timeInMillis, medname)

                    buildNotification(context, medname)

                }
            }

    }
    private fun buildNotification(context: Context, title: String) {
        Notify
            .with(context)
            .content {

                this.text = "It's time to take your " + title
            }

            .show()
    }
}