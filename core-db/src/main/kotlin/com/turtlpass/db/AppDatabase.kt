package com.turtlpass.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.turtlpass.db.dao.WebsiteEventDao
import com.turtlpass.db.entities.WebsiteEventEntity

@Database(entities = [WebsiteEventEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun websiteEventDao(): WebsiteEventDao
}
