package com.medicationtracker.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.app.AlertDialog
import android.text.method.TextKeyListener.clear
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.CollectionReference
import com.medicationtracker.app.dto.Medication
import kotlinx.android.synthetic.main.activity_addmedication.*
import java.util.*

class TakeMedicationFromNotification : AppCompatActivity(){
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var medicationsRef: CollectionReference = firestore.collection("medications")
    var medications: MutableLiveData<List<Medication>> = MutableLiveData<List<Medication>>()

    var classAlarmReceiver = AlarmReceiver()



    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.take_med_from_notification)

        setContent{
            MainScreen()
        }

    }

    @Composable
    fun MainScreen() {
        val context = LocalContext.current
        val openDialog = remember { mutableStateOf(false) }
        var isVisible by remember { mutableStateOf(true) }

        if (isVisible) {
            MedicationRow()
        }

    }
    @Composable
    fun MedicationRow() {

        val medications_ = remember { mutableStateListOf(Medication()) }

        fetchMedications(medications_)

        Row(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .padding(top = 10.dp)

        ) {
            LazyColumn() {
                items(medications_) { item: Medication ->
                    MedicationListItem(item)
                }
            }
        }
    }

    private fun fetchMedications(medications: SnapshotStateList<Medication>){

        medicationsRef.whereEqualTo("name", intent.getStringExtra("message").toString()).get()
            .addOnSuccessListener {
                medications.updateList(it.toObjects(Medication::class.java))
            }.addOnFailureListener{
                medications.updateList(listOf())
            }

    }
    private fun <T> SnapshotStateList<T>.updateList(medicationList: List<T>) {
        clear()
        addAll(medicationList)
    }
    @Composable
    fun MedicationListItem(medication: Medication) {
        val isVisible by remember { mutableStateOf(true) }

        if (isVisible) {
            Card(
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .fillMaxWidth(),
                elevation = 8.dp,
                backgroundColor = Color.LightGray,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color.Black)
            )
            {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Column(
                        modifier = Modifier
                            .weight(6f)
                            .padding(start = 2.dp)
                    ) {
                        Text(
                            text = "Medication Name: ${medication.name}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Doses Left: ${medication.doseAmount}", fontSize = 16.sp)
                    }
                    Column(
                        modifier = Modifier
                            .weight(1.5f)
                    ) {
                        Button(
                            onClick = { takeMedication(medication) },
                            modifier = Modifier
                                .padding(0.dp)
                                .width(80.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    12,
                                    121,
                                    230
                                )
                            )
                        ) {
                            Text(text = "Take", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                    }

                }
            }
        }
    }

    fun takeMedication( medication: Medication){
        //classAlarmReceiver.timer.cancel()
        if (medication.doseAmount > 0) {
            val newDoses = medication.doseAmount - 1
            firestore.collection("medications").document(medication.id).update("doseAmount", newDoses)
            Toast.makeText(this@TakeMedicationFromNotification, "${medication.name} taken. ${newDoses} doses left.", Toast.LENGTH_SHORT)
                .show()
            if (newDoses < 6){
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("${medication.name} Running Low")
                dialogBuilder.setMessage("${newDoses} left")
                dialogBuilder.setNegativeButton("Dismiss"){dialogInterface, which ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                dialogBuilder.show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("${medication.name} Out of Doses!")
            dialogBuilder.setMessage("You cannot take this medication because there are no more doses left.")
            dialogBuilder.setNegativeButton("Dismiss"){dialogInterface, which ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            dialogBuilder.show()

        }

    }


}