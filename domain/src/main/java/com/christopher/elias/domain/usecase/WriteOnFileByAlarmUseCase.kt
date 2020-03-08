package com.christopher.elias.domain.usecase

import com.christopher.elias.domain.repository.FileRepository

/**
 * Created by Christopher Elias on 8/03/2020.
 * celias@peruapps.com.pe
 *
 * Peru Apps
 * Lima, Peru.
 **/
class WriteOnFileByAlarmUseCase(private val repository: FileRepository) : BaseUseCaseNoParams<Unit>() {

    override suspend fun run() = repository.writeCurrentTimeOnFile(fromWorker = false)
}