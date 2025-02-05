package com.deep.system

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast



class MyApp : Application() {


    private val broadcastActions = listOf(
        Intent.ACTION_POWER_CONNECTED,
        Intent.ACTION_POWER_DISCONNECTED,
        Intent.ACTION_BOOT_COMPLETED,
        Intent.ACTION_SCREEN_ON,
        Intent.ACTION_SCREEN_OFF,
        Intent.ACTION_PACKAGE_ADDED,
        Intent.ACTION_PACKAGE_REMOVED,
        Intent.ACTION_PACKAGE_REPLACED,
        Intent.ACTION_BATTERY_LOW,
        Intent.ACTION_BATTERY_OKAY,
        Intent.ACTION_AIRPLANE_MODE_CHANGED,
        Intent.ACTION_HEADSET_PLUG,
        Intent.ACTION_MEDIA_MOUNTED,
        Intent.ACTION_MEDIA_UNMOUNTED,
        Intent.ACTION_TIME_TICK,
        Intent.ACTION_TIME_CHANGED,
        Intent.ACTION_DATE_CHANGED,
        Intent.ACTION_CONFIGURATION_CHANGED,
        Intent.ACTION_LOCALE_CHANGED,
        Intent.ACTION_DREAMING_STARTED,
        Intent.ACTION_DREAMING_STOPPED,
        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
        Intent.ACTION_MEDIA_SCANNER_STARTED,
        Intent.ACTION_MEDIA_SCANNER_FINISHED,
        Intent.ACTION_UID_REMOVED,
        Intent.ACTION_WALLPAPER_CHANGED,
        Intent.ACTION_PACKAGE_RESTARTED,
    )

    override fun onCreate() {
        super.onCreate()
        //checkBroadcastReceivers()
    }

    private fun checkBroadcastReceivers() {
        val packageManager = packageManager
        val packageName = packageName

        for (action in broadcastActions) {
            // Build intent for this action
            val intent = Intent(action)

            // Query if any broadcast receivers are registered for this action
            val receivers = packageManager.queryBroadcastReceivers(intent, PackageManager.MATCH_ALL)

            // Check if any receiver for this action belongs to this app
            val isRegisteredForApp = receivers.any { it.activityInfo.packageName == packageName }

            // Prepare message
            val message = if (isRegisteredForApp) {
                "Registered: $action"
            } else {
                "Not Registered: $action"
            }

            // Display the result in log and toast
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d("BroadcastCheck", message)
        }
    }
}
