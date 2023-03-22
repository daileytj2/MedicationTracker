package com.medicationtracker.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class Notification : AppCompatActivity() {
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationID = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pushnotificationui)

        createNotificationChannel()

        val button = findViewById<Button>(R.id.NotificationTest)
        button.setOnClickListener(){
            sendNotification()
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notificaiton Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description= descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
    private fun sendNotification(){
        val intent = Intent(this,Notification::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this,0,intent,0)
        val bimap = BitmapFactory.decodeResource(application.resources, R.drawable.stupidchilibot)
        val bimaplargeicon = BitmapFactory.decodeResource(application.resources, R.drawable.praisethesun)


        val builder = NotificationCompat.Builder(this@Notification, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Take Your Meds")
            .setContentTitle("It is time to take your Ibuprofen")
            .setLargeIcon(bimaplargeicon)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bimap))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this )) {
            notify(notificationID, builder.build())
        }

    }
}