package com.christopher.elias.workmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.christopher.elias.domain.usecase.WriteOnFileUseCase
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class WriteOnFileWorker(context: Context,
                        workerParams: WorkerParameters) : CoroutineWorker(context, workerParams), KoinComponent {

    companion object {
        private const val TAG = "WriteOnFileWorker"
    }
    private val writeOnFileUseCase : WriteOnFileUseCase by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.i(TAG, "do work")
        val deferred = async {
            Log.i(TAG, "Invoke writeOnFileUseCase")
            writeOnFileUseCase.invoke(this) {
                it.either({ failure ->
                    Log.e(TAG, "failure: $failure")
                    Result.failure()
                }, {
                    Log.i(TAG, "Content was wrote.")
                })
            }
        }
        Log.i(TAG, "await for result")
        deferred.await()
        deferred.cancel()
        Log.i(TAG, "deferred job canceled. sent Result Success.")
        Result.success()
    }
}