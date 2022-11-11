package com.medicationtracker.app

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals("com.tester.alarmmanager")){
            var b=intent.extras
            val notifyMe=TJNotifications()
            notifyMe.Notify(context!!, b!!.getString("message")!!,10)
        }
        else if(intent!!.action.equals("android.intent.action.BOOT_COMPLETED")){

            val saveData=SaveAlarmData(context!!)
            saveData.setAlarm()
        }



    }
}