package com.tanya.base.base

sealed class InvokeStatus
object InvokeStarted: InvokeStatus()
object InvokeSuccess: InvokeStatus()
data class InvokeError(val throwable: Throwable): InvokeStatus()