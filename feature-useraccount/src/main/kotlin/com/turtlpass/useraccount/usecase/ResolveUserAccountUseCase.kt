package com.turtlpass.useraccount.usecase

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract
import com.turtlpass.di.IoDispatcher
import com.turtlpass.useraccount.BuildConfig
import com.turtlpass.useraccount.model.UserAccount
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest

class ResolveUserAccountUseCase @Inject constructor(
    @param:IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val getContactIdUseCase: GetContactIdUseCase,
    private val contentResolver: ContentResolver,
) {
    suspend operator fun invoke(accountId: String): UserAccount {
        return findContactThumbnail(accountId)?.let {
            UserAccount(accountId = accountId, thumbnailUri = it)

        } ?: run {
            val gravatarUrl = getGravatarUrl(accountId)
            val avatarUrl = if (isGravatarAvailable(gravatarUrl)) {
                gravatarUrl
            } else {
                RANDOM_AVATAR_URL
            }
            UserAccount(accountId = accountId, avatarUrl = avatarUrl)
        }
    }

    private fun findContactThumbnail(accountId: String): Uri? =
        getContactIdUseCase(accountId)
            ?.let { id ->
                ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id)
                    .let { contactUri ->
                        Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY)
                    }
            }
            ?.takeIf { uri ->
                try {
                    contentResolver.openInputStream(uri)?.use { true } ?: false
                } catch (_: Exception) {
                    false
                }
            }

    /**
     * Output ex: https://s.gravatar.com/avatar/84c1c149c46b921d221f3febf47ad404?s=200&d=404
     */
    private fun getGravatarUrl(email: String, size: Int = 200): String =
        // 'd=404' will return 404 if no image exists
        "https://${BuildConfig.GRAVATAR_HOST}/avatar/${email.md5()}?s=$size&d=404"

    private fun String.md5(): String {
        return MessageDigest.getInstance("MD5")
            .digest(this.toByteArray())
            .joinToString(separator = "") {
                ((it.toInt() and 0xff) + 0x100)
                    .toString(16)
                    .substring(1)
            }
    }

    suspend fun isGravatarAvailable(gravatarUrl: String): Boolean = withContext(dispatcher) {
        try {
            val connection = URL(gravatarUrl).openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            val responseCode = connection.responseCode
            connection.disconnect()
            responseCode == 200
        } catch (e: Exception) {
            false
        }
    }

    private companion object {
        const val RANDOM_AVATAR_URL = "https://${BuildConfig.AVATAR_LIARA_HOST}/public"
    }
}
