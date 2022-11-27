package com.medicationtracker.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.medicationtracker.app.dto.Medication
import kotlinx.android.synthetic.main.activity_addmedication.*

class AddMedication : AppCompatActivity() {

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


}