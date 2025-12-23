package com.turtlpass.db.repository

import com.turtlpass.db.dao.WebsiteEventDao
import com.turtlpass.db.entities.WebsiteEventEntity
import com.turtlpass.model.ObservedAccessibilityEvent
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class WebsiteRepository @Inject constructor(
    private val dao: WebsiteEventDao
) {
    suspend fun insert(event: ObservedAccessibilityEvent.UrlEvent) {
        val entity = event.toEntity()
        dao.insert(entity)
    }

    fun observeLatest(limit: Int): Flow<List<WebsiteEventEntity>> {
        return dao.getLatest(limit)
    }

    suspend fun deleteById(id: String) {
        dao.deleteById(id)
    }

    suspend fun deleteByUrlAndTimestamp(url: String, timestamp: Long) {
        dao.deleteByUrlAndTimestamp(url, timestamp)
    }

    suspend fun clearAll() {
        dao.deleteAll()
    }
}
