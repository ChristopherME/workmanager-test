package com.christopher.elias.workmanager.ui.destinations.activities.main

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import com.christopher.elias.workmanager.R
import com.christopher.elias.workmanager.receiver.FileBroadcastReceiver
import com.christopher.elias.workmanager.worker.WriteOnFileWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 6969
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvStartWorker.setOnClickListener {
            requestWriteAndReadPermissions()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.any {permission -> permission == PackageManager.PERMISSION_DENIED }) {
                requestWriteAndReadPermissions()
            } else {
                startWriters()
            }
        }
    }

    private fun requestWriteAndReadPermissions() {
        if (verifyPermissionsGranted()) {
            startWriters()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun verifyPermissionsGranted() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun startWriters() {
        startPeriodicWorker()
        startAlarm()
    }

    private fun startPeriodicWorker() {
        val saveRequest = PeriodicWorkRequestBuilder<WriteOnFileWorker>(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("WRITE-ON-FILE-WORKER", ExistingPeriodicWorkPolicy.KEEP, saveRequest)
        Toast.makeText(this, "WRITE WORKER START", Toast.LENGTH_LONG).show()
    }

    private fun startAlarm() {
        // Set the alarm to start at approximately 4:00 p.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 16)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(this, FileBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(this, "WRITE ALARM START", Toast.LENGTH_LONG).show()
    }
}
