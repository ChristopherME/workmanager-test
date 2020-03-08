package com.christopher.elias.workmanager.ui.destinations.activities.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import com.christopher.elias.workmanager.R
import com.christopher.elias.workmanager.worker.WriteOnFileWorker
import kotlinx.android.synthetic.main.activity_main.*
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
                startPeriodicWorker()
            }
        }
    }

    private fun requestWriteAndReadPermissions() {
        if (verifyPermissionsGranted()) {
            startPeriodicWorker()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun verifyPermissionsGranted() = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun startPeriodicWorker() {
        Toast.makeText(this, "WRITE WORKER START", Toast.LENGTH_LONG).show()
        val saveRequest = PeriodicWorkRequestBuilder<WriteOnFileWorker>(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("WRITE-ON-FILE-WORKER", ExistingPeriodicWorkPolicy.KEEP, saveRequest)
    }

    private fun startAlarm() {

    }
}
