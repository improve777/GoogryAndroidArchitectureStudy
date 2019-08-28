package dev.daeyeon.gaasproject.module

import org.koin.dsl.module

val networkModule = module {

    single { NetworkManager.instance }
}