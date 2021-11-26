package com.tanya.data.android.repository.database

import androidx.room.withTransaction
import com.tanya.data.DatabaseTransactionRunner
import javax.inject.Inject

class RoomTransactionRunner @Inject constructor(
    private val db: ShowhubRoomDatabase
) : DatabaseTransactionRunner {
    override suspend operator fun <T> invoke(block: suspend () -> T) =
        db.withTransaction {
            block()
        }
}