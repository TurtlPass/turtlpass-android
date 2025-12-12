package com.turtlpass.appmanager.usecase

import com.google.common.net.InternetDomainName
import timber.log.Timber
import javax.inject.Inject

class ExtractAppTopLevelDomainUseCase @Inject constructor(
) {
    operator fun invoke(packageName: String): String {
        return try {
            val reversePackageName = reversePackageName(packageName)
            val getTopPrivateDomain = getTopLevelDomain(reversePackageName)
            val removeTopLevelDomain = removeTopLevelDomain(getTopPrivateDomain)
            removeTopLevelDomain
        } catch (e: Exception) {
            Timber.e(e)
            ""
        }
    }

    private fun reversePackageName(packageName: String): String {
        val packageParts = packageName.split(".")
        if (packageParts.size < 2) {
            return packageName
        }
        return packageParts.reversed().joinToString(".")
    }

    private fun getTopLevelDomain(hostname: String): String {
        return try {
            InternetDomainName.from(hostname).topPrivateDomain().toString()
        } catch (ignore: Exception) {
            val domainParts = hostname.split(".")
            if (domainParts.size > 1) {
                // last domain part
                domainParts[domainParts.size - 1]
            } else {
                hostname
            }
        }
    }

    private fun removeTopLevelDomain(domain: String): String {
        val domainParts = domain.split(".")
        return if (domainParts.size < 2) {
            domain
        } else {
            domainParts[0]
        }
    }
}