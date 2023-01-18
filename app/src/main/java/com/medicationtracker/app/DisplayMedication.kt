package com.medicationtracker.app

import android.os.Bundle
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
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.medicationtracker.app.dto.Medication

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
                .size(width = 400.dp, height = 500.dp)
                .padding(top = 10.dp)

        ) {
            LazyColumn() {
                items(medications_) { item: Medication ->
                    MedicationListItem(item)
                }
            }
        }
    }
    fun fetchMedications(medications: SnapshotStateList<Medication>){

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

                }
            }
        }
    }
}


