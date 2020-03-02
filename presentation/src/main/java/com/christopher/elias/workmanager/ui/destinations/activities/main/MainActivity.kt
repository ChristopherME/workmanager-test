package com.christopher.elias.workmanager.ui.destinations.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.christopher.elias.workmanager.R
import com.christopher.elias.workmanager.worker.WriteOnFileWorker
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvStartWorker.setOnClickListener {
            startPeriodicWorker()
        }
    }

    private fun startPeriodicWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val saveRequest = PeriodicWorkRequestBuilder<WriteOnFileWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("WRITE-ON-FILE-WORKER", ExistingPeriodicWorkPolicy.KEEP, saveRequest)
    }
}
