package com.christopher.elias.data.local

import android.content.Context
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

    override suspend fun write(): Either<Failure, Unit> {
        return withContext(Dispatchers.IO) {
            val loggerRoot = File(context.getExternalFilesDir(null), "worker-logger")
            // If logger file doesn't exist yet then...
            if (!loggerRoot.exists()) {
                // write the bastard.
                loggerRoot.mkdirs()
            }
            val loggerFile = File(loggerRoot, "logger.txt")
            val bufferedWriter = BufferedWriter(FileWriter(loggerFile, true))
            try {
                val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
                bufferedWriter.write("\nFile writing at: ${dateFormat.format(Date())}")
            } catch (e: Exception) {
                Either.Left(Failure.FileWriteException(e.message?:"Something happened while writing the file"))
            } finally {
                bufferedWriter.close()
            }
            Either.Right(Unit)
        }
    }
}