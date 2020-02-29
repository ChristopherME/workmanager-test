package com.christopher.elias.domain.repository

import com.christopher.elias.domain.entity.Either
import com.christopher.elias.domain.entity.Failure

interface FileRepository {
    suspend fun writeCurrentTimeOnFile() : Either<Failure, Unit>
}