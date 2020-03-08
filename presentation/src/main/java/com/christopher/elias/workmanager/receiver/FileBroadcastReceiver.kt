package com.christopher.elias.workmanager.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.christopher.elias.workmanager.services.WriteOnFileService
import java.util.*

/**
 * Created by Christopher Elias on 8/03/2020.
 * celias@peruapps.com.pe
 *
 * Peru Apps
 * Lima, Peru.
 **/
class FileBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            // Boot was completed so we have to re-schedule alarm
            // Set the alarm to start at approximately 4:00 p.m.
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 16)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            val am = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context, FileBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0)
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        } else {
            context?.startService(Intent(context, WriteOnFileService::class.java))
        }
    }
}