package com.christopher.elias.base

import android.app.Application
import com.christopher.elias.data.di.*
import com.christopher.elias.domain.di.useCasesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Christopher Elias on 3/02/2020.
 * christopher.mike.96@gmail.com
 *
 * Peru Apps
 * Lima, Peru.
 **/
class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TodoApplication)
            modules(arrayListOf(localLoggerModule, repositoryModule, useCasesModule))
        }
    }
}