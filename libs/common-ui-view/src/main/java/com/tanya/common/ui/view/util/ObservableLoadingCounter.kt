package com.tanya.common.ui.view.util

import com.tanya.base.base.InvokeStatus
import kotlinx.coroutines.flow.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Observes the loading state of an observable by tracking
 * the value on an atomically incremented/decremented counter
 */
class ObservableLoadingCounter {
    /**
     * A counter that is atomically incremented/decremented
     * Mainly used to switch between 0 and 1 denoting false and true respectively
     */
    private val count = AtomicInteger()

    /**
     * keeps track of the value on [count]
     */
    private val loadingState = MutableStateFlow(count.get())

    /**
     * Transforms the value in [loadingState] by setting it to true
     * if it's greater than 0 and false otherwise
     *
     * returns a flow of booleans
     */
    val observable: Flow<Boolean> get() =
        loadingState.map { it > 0 }.distinctUntilChanged()

    /**
     * increments the [count] value by 1 and sets the [loadingState] value
     * to the updated [count]
     */
    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    /**
     * decrements the [count] value by 1 and sets the [loadingState] value
     * to the updated [count]
     */
    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}

/**
 * Sets the loading state before the flow starts to be collected
 * and after it has completed or cancelled
 */
suspend fun Flow<InvokeStatus>.collectInto(counter: ObservableLoadingCounter) =
    onStart { counter.addLoader() }
        .onCompletion { counter.removeLoader() }
        .collect()