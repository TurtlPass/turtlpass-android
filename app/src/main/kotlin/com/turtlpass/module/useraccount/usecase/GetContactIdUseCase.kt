package com.turtlpass.module.useraccount.usecase

import android.content.ContentResolver
import android.provider.ContactsContract
import javax.inject.Inject

/**
 * Get Android Contact ID given associated Email
 */
class GetContactIdUseCase @Inject constructor(
    private val contentResolver: ContentResolver,
) {
    operator fun invoke(accountId: String): Long? {
        contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Email.CONTACT_ID),
            "${ContactsContract.CommonDataKinds.Email.ADDRESS}='$accountId'",
            null,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) return cursor.getLong(0)
        }
        return null
    }
}
