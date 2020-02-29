package com.christopher.elias.domain.usecase

import com.christopher.elias.domain.entity.Either
import com.christopher.elias.domain.entity.Failure
import kotlinx.coroutines.*

abstract class BaseUseCaseNoParams<out Result> where Result : Any {

    private lateinit var parentJob : Job

    abstract suspend fun run(): Either<Failure, Result>

    open operator fun invoke(
        scope: CoroutineScope,
        onResult: (Either<Failure, Result>) -> Unit = {}
    ) {
        /*
         * Credits to Paulo.
         * https://proandroiddev.com/i-exchanged-rxjava-for-coroutines-in-my-android-application-why-you-probably-should-do-the-same-5526dfb38d0e#cf27
         *
         * Basically. All exceptions that could occur while invoking the service will be handled on EndPointImpl because
         * the response.call function is wrapped inside a try catch block. If something else occur outside of that block (like Data -> Domain mappers)
         * those exceptions will be caught here. On the Launch scope.
         */
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            onResult(Either.Left(Failure.DataToDomainMapperFailure(throwable.message)))
            parentJob.cancelChildren()
            parentJob.cancel()
        }
        val backgroundJob = scope.async(Dispatchers.IO) { run() }
        parentJob = scope.launch(exceptionHandler) { onResult(backgroundJob.await()) }

    }
}