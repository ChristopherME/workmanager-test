package com.christopher.elias.base.ui.destinations.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import com.christopher.elias.base.R
import com.christopher.elias.base.worker.WriteOnFileWorker
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

    private fun startOneTimeWorker() {
        Log.i("Main", "startWorker")
        val writeOnFileWorker = OneTimeWorkRequestBuilder<WriteOnFileWorker>()
            .build()
        WorkManager.getInstance(this).enqueue(writeOnFileWorker)

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
