package com.turtlpass.db.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "website_events",
    indices = [Index(value = ["url", "timestamp"])]
)
data class WebsiteEventEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val url: String,
    val packageName: String,
    val timestamp: Long
)
