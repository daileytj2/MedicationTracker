package com.medicationtracker.app

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)

    lateinit var btnAlarm: Button
    lateinit var timePicker: TimePicker

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Values for email and phone number
        val rejectButton = findViewById<Button>(R.id.btnReject)
        val acceptButton = findViewById<Button>(R.id.btnAccept)
        val emailAddress = findViewById<EditText>(R.id.etEmailAddress)
        val phoneNumber = findViewById<EditText>(R.id.etPhoneNumber)
        val smsManager:SmsManager = SmsManager.getDefault()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val formatted = current.format(formatter)

        //Values for alarm
        val alarmTimePicker = findViewById<TimePicker>(R.id.timePicker)
        val alarmButton = findViewById<Button>(R.id.btnAlarm)

        //Reject Button
        rejectButton.setOnClickListener{

            var message = "The patient has not taken their medication."
            var address = emailAddress.text.toString()
            var subject = "MedTracker"
            var phone = phoneNumber.text.toString()


            if (address == "" && phone == ""){
                Toast.makeText(
                    this@MainActivity,
                    "Please, enter email address or phone number!",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                //phone
                smsManager.sendTextMessage(phone, null, message, null, null)

                //email
//                val intent = Intent(Intent.ACTION_SEND)
//                intent.putExtra(Intent.EXTRA_EMAIL, address)
//                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
//                intent.putExtra(Intent.EXTRA_TEXT, message)
//                intent.type = "message/rfc822"
//                startActivity(Intent.createChooser(intent, "Select email"))

                //successful
                Toast.makeText(
                    this@MainActivity,
                    "Message sent",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        //Accept Button
        acceptButton.setOnClickListener{

            var message = "The patient has taken their medication on $formatted."
            var address = emailAddress.text.toString()
            var subject = "MedTracker"
            var phone = phoneNumber.text.toString()


            if (address == "" && phone == ""){
                Toast.makeText(
                    this@MainActivity,
                    "Please, enter email address or phone number!",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                //phone
                smsManager.sendTextMessage(phone, null, message, null, null)

                //email
//                val intent = Intent(Intent.ACTION_SEND)
//                intent.putExtra(Intent.EXTRA_EMAIL, address)
//                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
//                intent.putExtra(Intent.EXTRA_TEXT, message)
//                intent.type = "message/rfc822"
//                startActivity(Intent.createChooser(intent, "Select email"))

                //successful
                Toast.makeText(
                    this@MainActivity,
                    "Message sent",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        alarmButton.setOnClickListener{
            val calendar: Calendar = Calendar.getInstance()
            if (Build.VERSION.SDK_INT >= 23) {
                calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.hour,
                    timePicker.minute,
                    0
                )
            } else {
                calendar.set(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH),
                    timePicker.currentHour,
                    timePicker.currentMinute, 0
                )
            }
            setAlarm(calendar.timeInMillis)
        }



    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, MyAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setRepeating(
            AlarmManager.RTC,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()
    }
    private class MyAlarm : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            Log.d("Alarm Bell", "Alarm just fired")
        }
    }

//    private fun createNotificationChannel() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.0){
//
//            val name : CharSequence = "medicationAlarm"
//            val description = "Alarm for medication"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(id: "alarmId", name, importance)
//            channel.description = description
//            val notificationManager = getSystemService(
//                NotificationManager::class.java
//            )
//        }
//    }
}