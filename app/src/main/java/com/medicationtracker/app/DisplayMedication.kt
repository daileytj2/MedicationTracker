package com.medicationtracker.app

import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.medicationtracker.app.dto.Medication
import com.medicationtracker.app.theme.UpdateMedicationDialog

class DisplayMedication : AppCompatActivity() {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var medications: MutableLiveData<List<Medication>> = MutableLiveData<List<Medication>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_displaymedications)

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
        val openDialog = remember { mutableStateOf(false) }
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
                        Text(text = "Medication Name: ${medication.name}", fontWeight = FontWeight.Bold)
                        Text(text = "Doses Left: ${medication.doseAmount}")
                        Text(text = "Expiration Date: ${medication.expDate}")
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Button(
                            onClick = { openDialog.value = true },
                            modifier = Modifier
                                .padding(2.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    12,
                                    121,
                                    230
                                )
                            )
                        ) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier
                                    .background(color = Color(12, 121, 230))
                            )
                        }
                        UpdateMedicationDialog(openDialog, medication.id)
                        Button(
                            onClick = {
                                deleteMedication(medication.id)
                            },
                            modifier = Modifier
                                .padding(2.dp),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(
                                    12,
                                    121,
                                    230
                                )
                            )
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier
                                    .background(color = Color(12, 121, 230))
                            )
                        }
                    }

                }
            }
        }
    }

    fun checkIfMedicationExists(documentID: String, medication: Medication){
        Log.d("Document", "Is not null ${documentID}")
        if(!documentID.equals(""))
            firestore.collection("medications").document(documentID)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val document = task.result
                        if (document.exists()) {
                            updateMedication(documentID, medication)
                            Log.d("TAG", "Document already exists. $documentID")
                        }
                    } else {
                        Log.d("An Error Occurred", "Try again")
                    }
                }
        else {
            saveMedications(medication)
            Log.d("TAG", "Document does NOT exist. $documentID")
        }
    }
    private fun updateMedication(documentID: String, medication: Medication){
        medication.id = documentID
        firestore.collection("medications").document(documentID)
            .set(medication)
    }

    fun deleteMedication(documentID: String) {
        firestore.collection("medications").document(documentID).delete()
    }

    fun saveMedications(medication: Medication){
        val document =  if (medication.id == null || medication.id.isEmpty()) {
            firestore.collection("medications").document()
        }
        else {
            firestore.collection("medications").document(medication.id!!)
        }
        medication.id = document.id
        val handle = document.set(medication)
        handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
        handle.addOnFailureListener { Log.e("Firebase", "Save Failed")}
    }
}


