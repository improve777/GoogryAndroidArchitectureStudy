package dev.daeyeon.gaasproject.base

import kotlinx.coroutines.Job


interface BaseCoroutineScope {
    val job: Job

    /**
     * Coroutine job cancel
     */
    fun releaseCoroutine()
}