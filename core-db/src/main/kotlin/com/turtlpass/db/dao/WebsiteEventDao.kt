package com.turtlpass.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.turtlpass.db.entities.WebsiteEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WebsiteEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: WebsiteEventEntity)

    @Query("SELECT * FROM website_events ORDER BY timestamp DESC LIMIT :limit")
    fun getLatest(limit: Int): Flow<List<WebsiteEventEntity>>

    // Delete a specific event by id
    @Query("DELETE FROM website_events WHERE id = :id")
    suspend fun deleteById(id: String)

    // Delete by URL
    @Query("DELETE FROM website_events WHERE url = :url")
    suspend fun deleteByUrl(url: String)

    // delete all events
    @Query("DELETE FROM website_events")
    suspend fun deleteAll()
}
