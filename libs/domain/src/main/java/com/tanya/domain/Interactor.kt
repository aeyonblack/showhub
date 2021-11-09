package com.tanya.domain

import com.tanya.base.base.InvokeError
import com.tanya.base.base.InvokeStarted
import com.tanya.base.base.InvokeStatus
import com.tanya.base.base.InvokeSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

abstract class Interactor<in P> {
    operator fun invoke(
        params: P,
        timeOutMs: Long = defaultTimeOutMs
    ) = flow {
        withTimeout(timeOutMs) {
            emit(InvokeStarted)
            doWork(params)
            emit(InvokeSuccess)
        }
    }.catch { emit(InvokeError(it)) }

    companion object {
        val defaultTimeOutMs = TimeUnit.MINUTES.toMillis(5)
    }

    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P)
}

abstract class ResultInteractor<in P, R> {
    operator fun invoke(params: P) = flow {
        emit(doWork(params))
    }

    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P): R
}