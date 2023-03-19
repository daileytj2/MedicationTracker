package com.medicationtracker.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
//import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import com.medicationtracker.app.dto.Medication
import com.medicationtracker.app.theme.UpdateMedicationDialog
import kotlinx.android.synthetic.main.activity_addmedication.*

class TakeMedication : AppCompatActivity(){
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var medications: MutableLiveData<List<Medication>> = MutableLiveData<List<Medication>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.takemedication)

        setContent{
            MainScreen()
        }
    }
    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MainScreen()
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

        firestore.collection("medications").get()
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
         val newDoses = medication.doseAmount - 1
         firestore.collection("medications").document(medication.id).update("doseAmount", newDoses)
         Toast.makeText(this@TakeMedication, "${medication.name} taken. ${newDoses} doses left.", Toast.LENGTH_SHORT)
             .show()
         if (newDoses < 6){
             val dialogBuilder = AlertDialog.Builder(this)

             // set message of alert dialog
             with(dialogBuilder)
             {
                 setTitle("${medication.name} Running Low")
                 setMessage("${newDoses} left")
                 setNegativeButton("Dismiss", null)
                 show()
             }
         }
         val intent = Intent(this@TakeMedication, MainActivity::class.java)
         // start your next activity
         startActivity(intent)
     }

}