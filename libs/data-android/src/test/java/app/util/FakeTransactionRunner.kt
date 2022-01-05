package app.util

import com.tanya.data.DatabaseTransactionRunner

class FakeTransactionRunner : DatabaseTransactionRunner {
    override suspend fun <T> invoke(block: suspend () -> T): T = block()
}