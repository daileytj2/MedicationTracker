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
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_addmedication.*
import kotlinx.android.synthetic.main.activity_main.*
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

//    private lateinit var btnSetAlarm: Button
//    private lateinit var timePicker: TimePicker

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
        val btnSetAlarm = findViewById<Button>(R.id.btnAlarm)
        val txtShowTime = findViewById<TextView>(R.id.txtShowTime)

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

        btnAdd.setOnClickListener(){
            val addMedScreen = Intent(this@MainActivity, AddMedication::class.java)
            startActivity(addMedScreen)
        }
    }

    fun BtnSetAlarm(view:View){
        val popAlarm= PopAlarm()
        val fm=supportFragmentManager
        popAlarm.show(fm,"Select time")
    }

    fun SetTime(Hours:Int,Minute:Int){

        txtShowTime.text= "$Hours:$Minute"

        val saveAlarmData=SaveAlarmData(applicationContext)
        saveAlarmData.SaveData(Hours, Minute)
        saveAlarmData.setAlarm()
    }


}