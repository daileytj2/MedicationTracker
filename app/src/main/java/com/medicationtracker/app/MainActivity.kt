package com.medicationtracker.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Button

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



//        val rejectButton = findViewById<Button>(R.id.btnReject)
//        val acceptButton = findViewById<Button>(R.id.btnAccept)
//        val medPhoneNum = Intent()
//        medPhoneNum.action = Intent.ACTION_SEND
//        medPhoneNum.type="text/plain"
//        medPhoneNum.putExtra(Intent.EXTRA_TEXT, "The recipient has not taken their medication.");
//        context.startActivity(Intent.createChooser(medPhoneNum,getString()))
//        rejectButton.setOnClickListener{
//            val smsManager: SmsManager
//        }
    }
}