package com.christopher.elias.workmanager.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.christopher.elias.domain.usecase.WriteOnFileByAlarmUseCase
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by Christopher Elias on 8/03/2020.
 * christopher.mike.96@gmail.com
 *
 * Peru Apps
 * Lima, Peru.
 **/
class WriteOnFileService : IntentService("WriteOnFileService"), KoinComponent {

    companion object {
        private const val TAG = "WriteOnFileService"
    }

    private var _parentJob = Job()
    private val _serviceScope = CoroutineScope(Dispatchers.IO + _parentJob)
    private val _writeOnFileByAlarmUseCase : WriteOnFileByAlarmUseCase by inject()

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "Executing write on file by alarm")
        executeWriteOnFileByAlarmUseCase()
    }

    private fun executeWriteOnFileByAlarmUseCase() = _serviceScope.launch {
        _writeOnFileByAlarmUseCase.invoke(this) {
            it.either({ failure ->
                Log.e(TAG, "Failure: $failure")
            }, {
                Log.e(TAG, "Wrote time on file success by alarm")
            })
        }
        Log.d(TAG, "outside of write on file use case invocation. Waiting for job to finish in order to cancel")
        _parentJob.cancelAndJoin()
        Log.d(TAG, "cancel parent job.")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }
}