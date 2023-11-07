package com.example.callingchannel

import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import android.os.Build
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

class CallForegroundService : Service() {
    private lateinit var telephonyManager: TelephonyManager
    private lateinit var phoneStateListener: PhoneStateListener

    override fun onCreate() {
        super.onCreate()
        telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                when (state) {
                    TelephonyManager.CALL_STATE_RINGING -> {
                        // Handle the incoming call here
                        showNotification()
                    }
                    // Handle other call states as needed
                }
            }
        }
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val name=intent?.getStringExtra("name")
        Toast.makeText(
            applicationContext, "Service has started running in the background",
            Toast.LENGTH_SHORT
        ).show()
        return START_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private fun displayFlutterDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Alert Title")
        alertDialog.setMessage("This is the message of the alert.")
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert) // Set an icon (optional)

        // Add a positive button and its click listener
        alertDialog.setPositiveButton("OK") { dialog, which ->
            // Perform an action when OK is clicked (e.g., close the dialog or do something)
            dialog.dismiss()
        }

        // Add a negative button and its click listener (optional)
        alertDialog.setNegativeButton("Cancel") { dialog, which ->
            // Perform an action when Cancel is clicked (e.g., close the dialog or do something)
            dialog.dismiss()
        }

        // Create and show the AlertDialog
        alertDialog.create().show()
    }

    private fun showNotification() {
        val channelId = "ForegroundServiceChannel"
        val notificationId = 1

        val notificationIntent = Intent(this, MainActivity::class.java) // Replace with your main activity
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Incoming Call")
            .setContentText("You have an incoming call.")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Foreground Service Channel", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
