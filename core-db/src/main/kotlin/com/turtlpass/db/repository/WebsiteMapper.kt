package com.turtlpass.db.repository

import com.turtlpass.db.entities.WebsiteEventEntity
import com.turtlpass.model.ObservedAccessibilityEvent

fun ObservedAccessibilityEvent.UrlEvent.toEntity(): WebsiteEventEntity {
    return WebsiteEventEntity(
        url = url,
        packageName = packageName,
        timestamp = timestamp
    )
}
