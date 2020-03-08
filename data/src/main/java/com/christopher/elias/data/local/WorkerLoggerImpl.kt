package com.christopher.elias.data.local

import android.content.Context
import android.util.Log
import com.christopher.elias.domain.entity.Either
import com.christopher.elias.domain.entity.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class WorkerLoggerImpl(private val context: Context) : WorkerLogger {
    
    companion object {
        private const val TAG = "WorkerLoggerImpl"
    }

    override suspend fun write(fromWorker: Boolean): Either<Failure, Unit> {
        return withContext(Dispatchers.IO) {
            val loggerRoot = if (fromWorker) {
                Log.i(TAG, "Write logger from WORKER")
                File(context.getExternalFilesDir(null), "worker-logger")
            } else {
                Log.i(TAG, "Write from ALARM")
                File(context.getExternalFilesDir(null), "alarm-logger")
            }
            // If logger file ROOT doesn't exist yet then...
            if (!loggerRoot.exists()) {
                Log.i(TAG, "logger file root doesn't exist. creating root...")
                // create ROOT of the bastard.
                loggerRoot.mkdirs()
            }
            val loggerFile = if (fromWorker) {
                File(loggerRoot, "logger-w.txt")
            } else {
                File(loggerRoot, "logger-a.txt")
            }
            val bufferedWriter = BufferedWriter(FileWriter(loggerFile, true))
            try {
                val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
                bufferedWriter.write("\nFile writing at: ${dateFormat.format(Date())}")
            } catch (e: Exception) {
                Either.Left(Failure.FileWriteException(e.message?:"Something happened while writing the file"))
            } finally {
                bufferedWriter.close()
            }
            Log.i(TAG, "logger file wrote successfully")
            Either.Right(Unit)
        }
    }
}