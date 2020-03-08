package com.christopher.elias.data.local

import com.christopher.elias.domain.entity.Either
import com.christopher.elias.domain.entity.Failure

interface WorkerLogger {
    suspend fun write(fromWorker: Boolean) : Either<Failure, Unit>
}