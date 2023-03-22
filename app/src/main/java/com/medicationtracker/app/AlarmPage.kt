package com.medicationtracker.app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.medicationtracker.app.service.AlarmService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alarmsidea.*
import java.util.*



class AlarmPage : AppCompatActivity(){
    lateinit var  alarmService: AlarmService
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarmsidea)
        alarmService =AlarmService(this)

        btnWeeklyAlarm2.setOnClickListener{
            setAlarm{ alarmService.setWeeklyAlarm(it, "place holder") }
        }

        btnDailyAlarm2.setOnClickListener{
            setAlarm{ alarmService.setDailyAlarm(it, "alarm page") }
        }
    }



    fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND,0)
            this.set(Calendar.MILLISECOND,0)
            DatePickerDialog(this@AlarmPage,
                0,
                {_, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)

                    TimePickerDialog(
                        this@AlarmPage,
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


        //txtShowTime.text= "$Hours:$Minute"

        val saveAlarmData=SaveAlarmData(applicationContext)
        saveAlarmData.SaveData(Hours, Minute)
        saveAlarmData.setAlarm()
    }
}