package com.christopher.elias.domain.usecase

import com.christopher.elias.domain.repository.FileRepository

class WriteOnFileUseCase(private val repository: FileRepository) : BaseUseCaseNoParams<Unit>() {

    override suspend fun run() = repository.writeCurrentTimeOnFile()
}