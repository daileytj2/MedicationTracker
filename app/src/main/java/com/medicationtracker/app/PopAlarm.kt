package com.medicationtracker.app


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

class PopAlarm : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var myView= inflater!!.inflate(R.layout.pop_alarm, container, false)

        var btnAlarmDone=myView.findViewById(R.id.btnAlarmDone) as Button
        var timePicker=myView.findViewById(R.id.timePicker) as TimePicker

        btnAlarmDone.setOnClickListener {

            val ma = activity as AlarmPage
            if (Build.VERSION.SDK_INT >= 23) {
                ma.setTime(timePicker.hour, timePicker.minute)
            } else {
                ma.setTime(timePicker.currentHour, timePicker.currentMinute)
            }

            this.dismiss()
        }

        return myView
    }
}