package com.tanya.domain

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tanya.base.base.InvokeError
import com.tanya.base.base.InvokeStarted
import com.tanya.base.base.InvokeSuccess
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
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

abstract class PagingInteractor<P : PagingInteractor.Params<T>, T : Any>
    : SubjectInteractor<P, PagingData<T>>() {
        interface Params<T:Any> {
            val pagingConfig: PagingConfig
        }
}

abstract class SuspendingWorkInteractor<P: Any, T> : SubjectInteractor<P,T>() {
    override fun createObservable(params: P) = flow {
        emit(doWork(params))
    }

    abstract suspend fun doWork(params: P): T
}

abstract class SubjectInteractor<P : Any, T> {
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow = paramState
        .distinctUntilChanged()
        .flatMapLatest { createObservable(it) }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>
}