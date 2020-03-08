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

    private val writeOnFileUseCase : WriteOnFileUseCase by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.i(this.javaClass.simpleName, "do work")
        val deferred = async {
            Log.i(this.javaClass.simpleName, "Invoke writeOnFileUseCase")
            writeOnFileUseCase.invoke(this) {
                it.either({ failure ->
                    Log.e(this.javaClass.simpleName, "failure: $failure")
                    Result.failure()
                }, {
                    Log.i(this.javaClass.simpleName, "Content was wrote.")
                })
            }
        }
        Log.i(this.javaClass.simpleName, "await for result")
        deferred.await()
        deferred.cancel()
        Log.i(this.javaClass.simpleName, "deferred job canceled. sent Result Success.")
        Result.success()
    }
}