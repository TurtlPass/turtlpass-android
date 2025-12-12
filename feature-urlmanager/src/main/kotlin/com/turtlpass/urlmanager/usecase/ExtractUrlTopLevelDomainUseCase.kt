package com.turtlpass.urlmanager.usecase

import com.google.common.net.InternetDomainName
import jakarta.inject.Inject
import timber.log.Timber

class ExtractUrlTopLevelDomainUseCase @Inject constructor() {

    operator fun invoke(input: String): String {
        return try {
            val hostname = extractHostname(input)
            val topPrivateDomain = getTopLevelDomain(hostname)
            getSecondLevelDomain(topPrivateDomain)
        } catch (e: Exception) {
            Timber.e(e)
            ""
        }
    }

    private fun extractHostname(input: String): String {
        return try {
            val uri = if (input.contains("://")) java.net.URI(input) else java.net.URI("http://$input")
            uri.host?.removePrefix("www.") ?: input.removePrefix("www.")
        } catch (e: Exception) {
            input.removePrefix("www.")
        }
    }

    private fun getTopLevelDomain(hostname: String): String {
        return try {
            // Use Guava InternetDomainName to get the top private domain
            InternetDomainName.from(hostname).topPrivateDomain().toString()
        } catch (ignore: Exception) {
            val parts = hostname.split(".")
            if (parts.size >= 2) {
                parts.takeLast(2).joinToString(".")  // fallback: last 2 parts
            } else {
                hostname
            }
        }
    }

    private fun getSecondLevelDomain(domain: String): String {
        val parts = domain.split(".")
        return if (parts.size >= 2) parts[0] else domain
    }
}
