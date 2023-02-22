package com.medicationtracker.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.medicationtracker.app.service.AlarmService
import kotlinx.android.synthetic.main.activity_addmedication.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.mainidea.*
import java.util.*


class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.mainidea)
//        alarmService =AlarmService(this)

        tomedication.setOnClickListener(){
            val addMedScreen = Intent(this, AddMedication::class.java)

            startActivity(addMedScreen)
        }
        toalarms.setOnClickListener(){
            val addAlarmScreen = Intent(this, AddAlarm::class.java)
            startActivity(addAlarmScreen)
        }
        tocontacts.setOnClickListener(){
            val addContactScreen = Intent(this, AddContact::class.java)
            startActivity(addContactScreen)
        }
        tohistory.setOnClickListener(){
            val addHistoryScreen = Intent(this, DisplayMedication::class.java)
            startActivity(addHistoryScreen)
        }

//        btnAlarmUI.setOnClickListener {
//            val addAlarmScreen = Intent(this@MainActivity, DisplayMedication::class.java)
//            startActivity(addAlarmScreen)
//        }
    }

}


