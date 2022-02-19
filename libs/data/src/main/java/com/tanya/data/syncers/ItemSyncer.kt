package com.tanya.data.syncers

import com.tanya.data.daos.EntityDao
import com.tanya.data.entities.BaseEntity

class ItemSyncer<LocalType : BaseEntity, NetworkType, Key>(
    private val insertEntity: suspend (LocalType) -> Long,
    private val updateEntity: suspend (LocalType) -> Unit,
    private val deleteEntity: suspend (LocalType) -> Int,
    private val localEntityToKey: suspend (LocalType) -> Key?,
    private val networkEntityToKey: suspend (NetworkType) -> Key,
    private val networkEntityToLocalEntity: suspend (NetworkType, Long?) -> LocalType,
) {
    suspend fun sync(
        currentValues: Collection<LocalType>,
        networkValues: Collection<NetworkType>,
        removeNotMatched: Boolean = true
    ): ItemSyncerResult<LocalType> {
        val currentDbEntities = ArrayList(currentValues)

        val removed = ArrayList<LocalType>()
        val added = ArrayList<LocalType>()
        val updated = ArrayList<LocalType>()

        for (networkEntity in networkValues) {

            val remoteId = networkEntityToKey(networkEntity) ?: break

            val dbEntityForId = currentDbEntities.find {
                localEntityToKey(it) == remoteId
            }

            if (dbEntityForId != null) {
                val entity = networkEntityToLocalEntity(networkEntity, dbEntityForId.id)
                if (dbEntityForId != entity) {
                    // This is currently in the DB, so lets merge it with the saved version
                    // and update it
                    updateEntity(entity)
                }
                // Remove it from the list so that it is not deleted
                currentDbEntities.remove(dbEntityForId)
                updated += entity
            } else {
                // Not currently in the DB, so lets insert
                added += networkEntityToLocalEntity(networkEntity, null)
            }
        }

        if (removeNotMatched) {
            // Anything left in the set needs to be deleted from the database
            currentDbEntities.forEach {
                deleteEntity(it)
                removed += it
            }
        }

        // Finally we can insert all of the new entities
        added.forEach {
            insertEntity(it)
        }

        return ItemSyncerResult(added, removed, updated)
    }
}

data class ItemSyncerResult<ET : BaseEntity>(
    val added: List<ET> = emptyList(),
    val deleted: List<ET> = emptyList(),
    val updated: List<ET> = emptyList()
)

fun <LocalType : BaseEntity, NetworkType, Key> syncerForEntity(
    entityDao: EntityDao<LocalType>,
    localEntityToKey: suspend (LocalType) -> Key?,
    networkEntityToKey: suspend (NetworkType) -> Key,
    networkEntityToLocalEntity: suspend (NetworkType, Long?) -> LocalType,
) = ItemSyncer(
    entityDao::insert,
    entityDao::update,
    entityDao::delete,
    localEntityToKey,
    networkEntityToKey,
    networkEntityToLocalEntity,
)

fun <Type : BaseEntity, Key> syncerForEntity(
    entityDao: EntityDao<Type>,
    entityToKey: suspend (Type) -> Key?,
    mapper: suspend (Type, Long?) -> Type,
) = ItemSyncer(
    entityDao::insert,
    entityDao::update,
    entityDao::delete,
    entityToKey,
    entityToKey,
    mapper,
)
