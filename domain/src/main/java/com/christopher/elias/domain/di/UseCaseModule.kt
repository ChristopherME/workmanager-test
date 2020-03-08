package com.christopher.elias.domain.di

import com.christopher.elias.domain.usecase.WriteOnFileByAlarmUseCase
import com.christopher.elias.domain.usecase.WriteOnFileUseCase
import org.koin.dsl.module

/**
 * Created by Christopher Elias on 27/01/2020.
 * christopher.mike.96@gmail.com
 *
 * Peru Apps
 * Lima, Peru.
 **/


val useCasesModule = module {
    factory { WriteOnFileUseCase(get()) }
    factory { WriteOnFileByAlarmUseCase(get()) }
}