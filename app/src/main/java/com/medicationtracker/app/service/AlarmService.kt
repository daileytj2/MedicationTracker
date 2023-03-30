package com.medicationtracker.app.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.medicationtracker.app.AlarmReceiver
import com.medicationtracker.app.DisplayMedication
import com.medicationtracker.app.MainActivity
import com.medicationtracker.app.TakeMedicationFromNotification
import com.medicationtracker.app.util.Constants
import com.medicationtracker.app.util.RandomIntUtil

class AlarmService(private val context: Context) {

    private val alarmManager: AlarmManager? =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun setExactAlarm(timeInMillis: Long){
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_EXACT_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                }
            ),

        )
    }

    //Every week
    fun setWeeklyAlarm(timeInMillis: Long, medname: String){
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_REPETITIVE_WEEKLY_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                        .putExtra("message", medname)
                }
            ),

        )
    }

    //Every day
    fun setDailyAlarm(timeInMillis: Long, medname: String){
        val intent=Intent(context, TakeMedicationFromNotification::class.java)
        setAlarm(
            timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_REPETITIVE_DAILY_ALARM
                    putExtra(Constants.EXTRA_EXACT_ALARM_TIME, timeInMillis)
                        .putExtra("message", medname)
                }
            ),


        )
        intent.action=null
        intent.putExtra("message", medname)
    }

    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent){


        alarmManager?.let {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )

            }else {

                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
            /*val intent2=Intent(context, DisplayMedication::class.java)

            intent2.action="com.tester.alarmmanager"
            intent2.putExtra("message", medname)
            Log.d("intent", "${intent2.action}")
            Log.d("extras", "${intent2.extras}")
            intent2.action=null

             */
        }

    }
    /*

     */

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent) =
        PendingIntent.getBroadcast(
            context,
            RandomIntUtil.getRandomInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT,

        )
}
