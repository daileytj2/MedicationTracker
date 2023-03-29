package com.medicationtracker.app

import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.medicationtracker.app.dto.Medication
import com.medicationtracker.app.service.AlarmService
import kotlinx.android.synthetic.main.activity_addmedication.*
import java.util.*

class AddMedication : AppCompatActivity() {
    lateinit var  alarmService: AlarmService
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var medications : MutableLiveData<List<Medication>> = MutableLiveData<List<Medication>>()

    init{
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmedication)

        btnAddMedication.setOnClickListener(){
           val medicationName = etMedicationName.text.toString()
           val doses = Integer.parseInt(etDoseAmount.text.toString())
           val rxNumber = etRXNumber.text.toString()
            val expiration = etExpDate.text.toString()

            val medication = Medication().apply {
                name = medicationName
                doseAmount = doses
                RXNumber = rxNumber
                expDate = expiration
            }
           saveMedication(medication)
            val returnToMain = Intent(this@AddMedication, MainActivity::class.java)
            startActivity(returnToMain)
        }

        btnCancelAdd.setOnClickListener {
            val returnToMain = Intent(this@AddMedication, MainActivity::class.java)
            startActivity(returnToMain)

        }
        alarmService =AlarmService(this)
        btnWeeklyAlarm3.setOnClickListener {
            val medicationName = etMedicationName.text.toString()
            if (etMedicationName != null){
                WeeklyAlarm(medicationName)
            }
            else{
                var toast = Toast.makeText(applicationContext, "ERROR: Please fill out the form", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        btnDailyAlarm3.setOnClickListener {
            val medicationName = etMedicationName.text.toString()
            if (etMedicationName != null){
                DailyAlarm(medicationName)
            }
            else{
                var toast = Toast.makeText(applicationContext, "ERROR: Please fill out the form", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    fun saveMedication(medication: Medication){
        val document = if(medication.id == null || medication.id.isEmpty())
            firestore.collection("medications").document()
        else
            firestore.collection("medications").document(medication.id)

        medication.id = document.id
        val handle = document.set(medication)
        handle.addOnSuccessListener { Log.d("Firebase", "Document saved!") }
        handle.addOnFailureListener{(Log.e("Firebase", "Document failed to save")) }

    }
    fun DailyAlarm(name:String){
        setAlarm{ alarmService.setDailyAlarm(it,name) }
    }
    fun WeeklyAlarm(name:String){
        setAlarm{ alarmService.setDailyAlarm(it,name) }
    }

    fun setAlarm( callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND,0)
            this.set(Calendar.MILLISECOND,0)

            DatePickerDialog(this@AddMedication,
                0,
                {_, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        this@AddMedication,
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
                this.get(Calendar.DAY_OF_MONTH),


                ).show()
        }
    }

}