package com.christopher.elias.data.repository

import com.christopher.elias.data.local.WorkerLogger
import com.christopher.elias.domain.entity.Either
import com.christopher.elias.domain.entity.Failure
import com.christopher.elias.domain.repository.FileRepository

class FileRepositoryImpl(private val logger : WorkerLogger) : FileRepository {

    override suspend fun writeCurrentTimeOnFile(fromWorker: Boolean): Either<Failure, Unit> {
        return when(val status = logger.write(fromWorker)){
            is Either.Left -> Either.Left(status.a)
            is Either.Right -> Either.Right(status.b)
        }
    }
}