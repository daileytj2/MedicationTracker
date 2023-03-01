package com.medicationtracker.app

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import com.medicationtracker.app.SaveAlarmData
import com.medicationtracker.app.TJNotifications
import io.karn.notify.Notify
import android.text.format.DateFormat
import com.medicationtracker.app.service.AlarmService
import com.medicationtracker.app.util.Constants
import io.karn.notify.internal.utils.Action
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)


        val smsManager: SmsManager = SmsManager.getDefault()
        var txtMessage = "The patient has not taken their medication."

        //destinationAddress needs to be a phone number that the user inputs
                smsManager.sendTextMessage("5138882059", null, txtMessage, null, null)

        //Below is code to send a text after ten minutes
//              Timer().schedule(600000) {
//                  smsManager.sendTextMessage("5138882059", null, message, null, null)
//              }

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
//                val smsManager: SmsManager = SmsManager.getDefault()
//                var txtMessage = "The patient has not taken their medication."

                //destinationAddress needs to be a phone number that the user inputs
//                smsManager.sendTextMessage("5138882059", null, txtMessage, null, null)

                //Below is code to send a text after ten minutes
//              Timer().schedule(600000) {
//                  smsManager.sendTextMessage("5138882059", null, message, null, null)

//              }
            }



        }
    }



    private fun buildNotification(context: Context, title: String, message: String){


        Notify
            .with(context)
            .meta { // this: Payload.Meta
                // Launch the MainActivity once the notification is clicked.
                clickIntent = PendingIntent.getActivity(context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0)
                // Start a service which clears the badge count once the notification is dismissed.
                clearIntent = PendingIntent.getService(context,
                    0,
                    Intent(context, AlarmReceiver::class.java)
                        .putExtra("action", "clear_badges"),
                    0)
            }
            .content {
                this.title = title
                this.text = "It's time to take medicine"
            }
            .actions{
                add(Action(
                    // The icon corresponding to the action.
                    R.drawable.ic_bell,
                    // The text corresponding to the action -- this is what shows below the
                    // notification.
                    "Dismiss",
                    // Swap this PendingIntent for whatever Intent is to be processed when the action
                    // is clicked.
                    PendingIntent.getService(context,
                        0,
                        Intent(context, AlarmReceiver::class.java)
                            .putExtra("action", "clear_badges"),
                        0)
                ))
            }
            .show()

    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
}