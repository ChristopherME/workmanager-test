package com.christopher.elias.data.di

import com.christopher.elias.data.local.WorkerLogger
import com.christopher.elias.data.local.WorkerLoggerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val localLoggerModule = module {
    single<WorkerLogger> { WorkerLoggerImpl(androidContext()) }
}