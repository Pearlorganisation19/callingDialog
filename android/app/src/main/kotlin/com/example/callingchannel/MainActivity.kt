package com.example.callingchannel

import android.content.Intent
import android.widget.Toast
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant


class MainActivity : FlutterActivity() {
   override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL)
            .setMethodCallHandler { call, result ->
                if (call.method.equals("startForegroundService")) {
                    Toast.makeText(this, "Foreground service started", Toast.LENGTH_SHORT).show()
                    startForegroundService()
                } else if (call.method.equals("stopForegroundService")) {
                    stopForegroundService()
                } else {
                    result.notImplemented()
                }
            }
    }

    private fun startForegroundService() {
        startService(Intent(this, CallForegroundService::class.java))
        Toast.makeText(this, "Foreground service started2", Toast.LENGTH_SHORT).show()
    }

    private fun stopForegroundService() {
        stopService(Intent(this, CallForegroundService::class.java))
    }

    companion object {
        private const val CHANNEL = "incoming_call_channel"
    }
}

