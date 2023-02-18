package com.medicationtracker.app.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.medicationtracker.app.DisplayMedication
import com.medicationtracker.app.dto.Medication
import java.lang.Integer.parseInt

@Composable
fun UpdateMedicationDialog(openDialog: MutableState<Boolean>, documentID: String) {

    val context = LocalContext.current
    var strSelectedData = ""
    val medName = remember { mutableStateOf("") }
    val doses = remember { mutableStateOf("") }
    val expiration = remember { mutableStateOf("") }

    if (openDialog.value) {
        Dialog(onDismissRequest = { openDialog.value = false }) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .height(580.dp)
                    .padding(5.dp),
                shape = RoundedCornerShape(5.dp),
                color = Color.LightGray
            ) {
                Column(
                    modifier = Modifier.padding(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Update Medication",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    OutlinedTextField(
                        value = medName.value,
                        onValueChange = { medName.value = it },
                        label = { Text(text = "Medication Name") },
                        placeholder = { Text(text = "Medication name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    OutlinedTextField(
                        value = doses.value,
                        onValueChange = { doses.value = it },
                        label = { Text(text = "Dose Amount") },
                        placeholder = { Text(text = "Dose Amount") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    OutlinedTextField(
                        value = expiration.value,
                        onValueChange = { expiration.value = it },
                        label = { Text(text = "Expiration Date") },
                        placeholder = { Text(text = "Expiration Date") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    Spacer(modifier = Modifier.padding(10.dp))

                    Spacer(modifier = Modifier.padding(15.dp))
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {

                        IconButton(onClick = {

                                val medication = Medication().apply {
                                    name = medName.value
                                    doseAmount = parseInt(doses.value)
                                    expDate = expiration.value
                                }

                                DisplayMedication().checkIfMedicationExists(documentID, medication)
                                openDialog.value = false

                        }

                        ) {
                            //Save Reminder Button
                            Icon(Icons.Filled.Check, null, tint = Color(5, 115, 34))
                        }
                        //Close Window/Cancel
                        IconButton(onClick = {openDialog.value = false}) {

                            Icon(Icons.Filled.Close, null, tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}


