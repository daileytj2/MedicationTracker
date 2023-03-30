package com.medicationtracker.app

import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.core.content.ContextCompat.getSystemService
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.os.CountDownTimer
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.text.format.DateFormat
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.medicationtracker.app.dto.Contact
import com.medicationtracker.app.dto.Medication
import com.medicationtracker.app.service.AlarmService
import com.medicationtracker.app.util.Constants
import io.karn.notify.Notify
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class AlarmReceiver: BroadcastReceiver() {

    val takeMedicine = TakeMedication()
    var classAddContact = AddContact()
    lateinit var timer: CountDownTimer

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var contacts: MutableLiveData<List<Contact>> = MutableLiveData<List<Contact>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    private fun fetchContacts(contacts: SnapshotStateList<Contact>){

        firestore.collection("contacts").get()
//            .addOnSuccessListener {
//                contacts.updateList(it.toObjects(Contact::class.java))
//            }.addOnFailureListener{
//                contacts.updateList(listOf())
//            }

    }

//    var timer = Timer();



    override fun onReceive(context: Context, intent: Intent) {

        Log.d("intent", "${intent.action}")
        var medname = intent.getStringExtra("message").toString()

        //createNotificationChannel(medname)
            val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)

            val smsManager: SmsManager = SmsManager.getDefault()
            var txtMessage = "The patient has not taken their medication."

            //destinationAddress needs to be a phone number that the user inputs
//            smsManager.sendTextMessage("5138882059", null, txtMessage, null, null)

            //Below is code to send a text after thirty minutes
            timer = object : CountDownTimer(1800000, 1000){
                override fun onTick(p0: Long) {

                }

                override fun onFinish() {
                    smsManager.sendTextMessage(classAddContact.etPhoneNumber.text.toString(), null, txtMessage, null, null)
                }

            }.start()

//            timer.schedule(1800000) {
//                smsManager.sendTextMessage("5138882059", null, txtMessage, null, null)
//                cancelTimer()
//            }




            when (intent.action) {
                Constants.ACTION_SET_EXACT_ALARM -> {
                    buildNotification(context, "Set Exact Time")
                }

                Constants.ACTION_SET_REPETITIVE_WEEKLY_ALARM -> {
                    val cal = Calendar.getInstance().apply {
                        this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
                    }
                    AlarmService(context).setWeeklyAlarm(cal.timeInMillis, medname)
                    buildNotification(context, medname)
                }

                Constants.ACTION_SET_REPETITIVE_DAILY_ALARM -> {
                    val cal = Calendar.getInstance().apply {
                        this.timeInMillis = timeInMillis + TimeUnit.HOURS.toMillis(24)
                    }
                    AlarmService(context).setDailyAlarm(cal.timeInMillis, medname)

                    buildNotification(context, medname)

                }
            }


    }


    private fun buildNotification(context: Context, title: String) {

        Notify
            .with(context)
            .meta {
                // Launch the MainActivity once the notification is clicked.
                clickIntent = PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, MainActivity::class.java),
                    0
                ) }
            .content {
                this.text = "It's time to take your " + title

            }
            .show()
         }

}
