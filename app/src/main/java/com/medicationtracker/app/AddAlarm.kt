package com.medicationtracker.app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.medicationtracker.app.service.AlarmService
import kotlinx.android.synthetic.main.activity_addalarm.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class AddAlarm : AppCompatActivity() {

    lateinit var alarmService: AlarmService
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addalarm)
        alarmService =AlarmService(this)

        btnWeeklyAlarm.setOnClickListener{
            setAlarm{ alarmService.setWeeklyAlarm(it, "place holder") }
        }

        btnDailyAlarm.setOnClickListener{
            setAlarm{ alarmService.setDailyAlarm(it, "place holder") }
        }
    }

    private fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND,0)
            this.set(Calendar.MILLISECOND,0)
            DatePickerDialog(
                this@AddAlarm,
                0,
                {_, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)

                    TimePickerDialog(
                        this@AddAlarm,
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



}