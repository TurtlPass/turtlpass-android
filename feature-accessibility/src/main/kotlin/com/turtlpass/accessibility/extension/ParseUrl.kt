package com.turtlpass.accessibility.extension

import android.util.Patterns
import com.google.common.net.InternetDomainName
import timber.log.Timber
import java.net.URL

fun parseUrl(urlString: String): String? {
    // Must be a real URL
    if (!Patterns.WEB_URL.matcher(urlString).matches()) {
        Timber.e("$urlString doesn't match a WEB URL")
        return null
    }
    // Normalize: add protocol if missing
    val normalized = if (urlString.startsWith("http", ignoreCase = true)) {
        urlString
    } else {
        "https://$urlString"
    }
    // Try to parse URL
    val host = try {
        URL(normalized).host.lowercase()
    } catch (e: Exception) {
        Timber.e(e)
        return null
    }
    return extractRegistrableDomain(host)
}

private fun extractRegistrableDomain(hostname: String): String {
    val cleanedHost = hostname.lowercase().trimEnd('.')

    // Check if it's an IPv4 or IPv6 address
    if (isIpAddress(cleanedHost)) return cleanedHost

    // strip noise (Brave/Firefox/Chrome heuristics)
    val hostWithoutNoise = stripNoiseSubdomains(cleanedHost)

    // try PSL via Guava
    try {
        return InternetDomainName.from(hostWithoutNoise)
            .topPrivateDomain()
            .toString()
    } catch (_: Exception) {
        // ignore
    }

    // PSL-like fallback
    return fallbackByPsl(hostWithoutNoise)
}

private fun isIpAddress(host: String): Boolean {
    return host.matches(Regex("""\d{1,3}(\.\d{1,3}){3}""")) ||  // IPv4
            host.contains(":") // crude IPv6 check
}

private val noiseSubdomains = setOf(
    // UI / Device
    "www", "m", "mobile", "amp", "beta", "preview", "stage", "dev", "test",

    // Localization (Firefox-style)
    "en", "de", "fr", "es", "it", "pt", "ru", "nl", "sv", "no", "da",
    "fi", "pl", "cs", "ja", "ko", "zh", "tr", "ar",

    // Tracking / Redirectors (Brave Tracker module)
    "l", "lm", "link", "redirect", "redir", "out", "outbound", "exit",
    "click", "track", "trk", "tracking", "ads", "ad", "affiliate",
    "tag", "tags", "utm", "campaign",

    // AMP / Mobile variants
    "amp", "ampcdn", "touch", "lite",

    // Portal / Generic
    "news", "blog", "web", "app", "apps",

    // Social redirector prefixes
    "l-facebook", "lm-facebook", "l-instagram", "lm-instagram"
)

private fun stripNoiseSubdomains(host: String): String {
    val parts = host.split(".").filter { it.isNotBlank() }
    return parts
        .dropWhile { it.lowercase() in noiseSubdomains }
        .joinToString(".")
        .ifBlank { host }
}

private val multiLevelTlds = setOf(
    // UK
    "co.uk", "org.uk", "gov.uk", "ac.uk", "ltd.uk",

    // AU
    "com.au", "net.au", "org.au", "edu.au",

    // JP
    "co.jp", "ne.jp", "or.jp",

    // BR
    "com.br", "net.br", "org.br",

    // NZ
    "co.nz", "ac.nz", "org.nz",

    // CA second-levels
    "gc.ca"
)

private fun fallbackByPsl(host: String): String {
    val parts = host.split(".").filter { it.isNotBlank() }

    if (parts.size <= 1) return host

    val lastTwo = parts.takeLast(2).joinToString(".")
    val lastThree = parts.takeLast(3).joinToString(".")

    return when {
        multiLevelTlds.contains(lastTwo) -> lastThree
        multiLevelTlds.contains(lastThree) -> lastThree
        else -> lastTwo
    }
}
