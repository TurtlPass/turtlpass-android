package com.turtlpass.module.useraccount.usecase

import android.content.ContentUris
import android.net.Uri
import android.provider.ContactsContract
import com.turtlpass.BuildConfig
import com.turtlpass.common.domain.Result
import com.turtlpass.di.IoDispatcher
import com.turtlpass.module.useraccount.model.UserAccount
import com.turtlpass.module.useraccount.repo.DeviceAccountsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.security.MessageDigest
import javax.inject.Inject

class UserAccountsUseCase @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val accountRepository: DeviceAccountsRepository,
    private val getContactIdUseCase: GetContactIdUseCase,
) {
    operator fun invoke(): Flow<Result<List<UserAccount>>> {
        return flow {
            emit(Result.Loading)
            try {
                val accountList = accountRepository.deviceRegisteredAccountEmails()
                    .map { account ->
                        getContactIdUseCase(account.name)?.let { contactId ->
                            ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI,
                                contactId
                            ).let { contactUri ->
                                Uri.withAppendedPath(
                                    contactUri,
                                    ContactsContract.Contacts.Photo.CONTENT_DIRECTORY
                                )?.let { thumbnailUri ->
                                    UserAccount(
                                        accountId = account.name,
                                        thumbnailUri = thumbnailUri,
                                    )
                                }
                            }
                        } ?: UserAccount(
                            accountId = account.name,
                            gravatarUrl = getGravatarUrl(account.name)
                        )
                    }
                if (accountList.isNotEmpty()) {
                    emit(Result.Success(accountList))
                } else {
                    emit(Result.Error())
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }.catch {
            emit(Result.Error(it))
        }.flowOn(dispatcher)
    }

    /**
     * Output ex: https://s.gravatar.com/avatar/84c1c149c46b921d221f3febf47ad606?s=200
     */
    private fun getGravatarUrl(email: String, size: Int = 200): String =
        "${BuildConfig.GRAVATAR_BASE_URL}/avatar/${email.md5()}?s=$size"

    private fun String.md5(): String {
        return MessageDigest.getInstance("MD5")
            .digest(this.toByteArray())
            .joinToString(separator = "") {
                ((it.toInt() and 0xff) + 0x100)
                    .toString(16)
                    .substring(1)
            }
    }
}
