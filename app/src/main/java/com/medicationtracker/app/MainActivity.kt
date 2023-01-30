package com.medicationtracker.app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.medicationtracker.app.service.AlarmService
import kotlinx.android.synthetic.main.activity_addmedication.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {


    lateinit var  alarmService: AlarmService
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        alarmService =AlarmService(this)

        
        val new = findViewById<Button>(R.id.notificationpage)
        new.setOnClickListener {
            val intent = Intent(this, Notification::class.java)
            // start your next activity
            startActivity(intent)
        }
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
//        val btnSetAlarm = findViewById<Button>(R.id.btnAlarm)
//        val txtShowTime = findViewById<TextView>(R.id.txtShowTime)

        btnWeeklyAlarm.setOnClickListener{
            setAlarm{ alarmService.setWeeklyAlarm(it) }
        }

        btnDailyAlarm.setOnClickListener{
            setAlarm{ alarmService.setDailyAlarm(it) }
        }

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

        btnAddMed.setOnClickListener(){
            val addMedScreen = Intent(this@MainActivity, AddMedication::class.java)
            startActivity(addMedScreen)
        }

        btnAddContact.setOnClickListener {
            val addContactScreen = Intent(this@MainActivity, AddContact::class.java)
            startActivity(addContactScreen)
        }

        btnViewMedications.setOnClickListener {
            val addViewMedicationScreen = Intent(this@MainActivity, DisplayMedication::class.java)
            startActivity(addViewMedicationScreen)
        }
    }

//    fun btnSetAlarm(view:View){
//        val popAlarm= PopAlarm()
//        val fm=supportFragmentManager
//        popAlarm.show(fm,"Select time")
//    }

    private fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND,0)
            this.set(Calendar.MILLISECOND,0)
            DatePickerDialog(
                this@MainActivity,
                0,
                {_, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)

                    TimePickerDialog(
                        this@MainActivity,
                        0,
                        {_, hour, min ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, min)
                            callback(this.timeInMillis)
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)

            ).show()
        }
    }


    fun setTime(Hours:Int,Minute:Int){

        txtShowTime.text= "$Hours:$Minute"

        val saveAlarmData=SaveAlarmData(applicationContext)
        saveAlarmData.SaveData(Hours, Minute)
        saveAlarmData.setAlarm()
    }


}