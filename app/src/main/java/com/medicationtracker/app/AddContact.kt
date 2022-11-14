package com.medicationtracker.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.medicationtracker.app.dto.Contact
import kotlinx.android.synthetic.main.activity_addcontact.*
import kotlinx.android.synthetic.main.activity_main.*

class AddContact : AppCompatActivity() {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var contacts : MutableLiveData<List<Contact>> = MutableLiveData<List<Contact>>()

    init{
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addcontact)

        btnSaveContact.setOnClickListener(){
            val contactName = etContactName.text.toString()
            val phoneNumber = etContactNumber.text.toString()
            val emailAddress = etContactEmail.text.toString()

            val contact = Contact().apply {
                name = contactName
                phone = phoneNumber
                email = emailAddress
            }
            saveContact(contact)
            val returnToMain = Intent(this@AddContact, MainActivity::class.java)
            startActivity(returnToMain)
        }

        btnCancelContact.setOnClickListener {
            val returnToMain = Intent(this@AddContact, MainActivity::class.java)
            startActivity(returnToMain)

        }
    }

    fun saveContact(contact: Contact){
        val document = if(contact.id == null || contact.id.isEmpty())
            firestore.collection("contacts").document()
        else
            firestore.collection("contacts").document(contact.id)

        contact.id = document.id
        val handle = document.set(contact)
        handle.addOnSuccessListener { Log.d("Firebase", "Document saved!") }
        handle.addOnFailureListener{(Log.e("Firebase", "Document failed to save")) }

    }


}

