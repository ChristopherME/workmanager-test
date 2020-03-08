package com.christopher.elias.domain.repository

import com.christopher.elias.domain.entity.Either
import com.christopher.elias.domain.entity.Failure

interface FileRepository {
    /**
     * @param fromWorker true if comes from worker. false if comes from alarm manager.
     */
    suspend fun writeCurrentTimeOnFile(fromWorker: Boolean) : Either<Failure, Unit>
}