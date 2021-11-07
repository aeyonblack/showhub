package com.tanya.data.daos

import com.tanya.data.Entry
import com.tanya.data.results.EntryWithShow

abstract class EntryDao<EC: Entry, LI: EntryWithShow<EC>> : EntityDao<EC>() {
    abstract suspend fun deleteAll()
}