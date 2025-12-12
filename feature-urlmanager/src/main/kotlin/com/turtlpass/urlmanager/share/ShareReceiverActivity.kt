package com.turtlpass.urlmanager.share

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.core.text.HtmlCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.di.moduleNavigation.ActivityLabel
import com.turtlpass.di.moduleNavigation.ModuleNavigation
import com.turtlpass.domain.parcelable
import com.turtlpass.domain.parcelableArrayList
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber


/**
 * Activity that receives and processes Android share intents containing URLs, text,
 * HTML fragments and URI lists.
 *
 * This activity is registered in the manifest with intent filters for
 * `ACTION_SEND` and `ACTION_SEND_MULTIPLE`, and supports a wide range of MIME
 * types used by Android apps when sharing URLs or link-containing content:
 *
 * Supported MIME types:
 *  - `text/plain`
 *  - `text/html`
 *  - `text/x-uri`
 *  - `text/uri-list`
 *  - `application/xhtml+xml`
 *  - `message/rfc822`
 *
 * Behavior:
 *  - For **single-item** shares (`ACTION_SEND`), the activity inspects the MIME
 *    type and routes the payload to the appropriate handler, extracting URLs from
 *    plain text, HTML, direct URI values, or email content.
 *  - For **multi-item** shares (`ACTION_SEND_MULTIPLE`), all URIs provided in
 *    `EXTRA_STREAM` are collected and passed to `handleMultipleUris`.
 *
 * URL Extraction:
 *  - Plain text URLs are detected using `Patterns.WEB_URL`.
 *  - HTML content is parsed for both `<a href="">` attributes and visible text URLs.
 *  - URI lists may arrive as newline-separated text, a single URI string, or
 *    Android `Uri` objects in `EXTRA_STREAM`.
 *
 * This class serves as a robust, general-purpose entry point for apps that need
 * to accept URLs from the Android sharesheet regardless of how the originating
 * application encodes or formats the shared data.
 */
@AndroidEntryPoint
class ShareReceiverActivity : AppCompatActivity() {

    @Inject
    lateinit var moduleNavigation: ModuleNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIncomingShare(intent)
        finish()
    }

    private fun handleIncomingShare(intent: Intent?) {
        if (intent == null) return

        when (intent.action) {
            Intent.ACTION_SEND -> handleSingleShare(intent)
            Intent.ACTION_SEND_MULTIPLE -> {
                intent.parcelableArrayList<Uri>(Intent.EXTRA_STREAM)
                    ?.let { handleMultipleUris(it) }
            }
        }
    }

    private fun handleSingleShare(intent: Intent) {
        when (intent.type) {

            "text/plain" -> intent.getStringExtra(Intent.EXTRA_TEXT)?.let { handleTextPlain(it) }

            "text/html" -> {
                val html = intent.getStringExtra(Intent.EXTRA_HTML_TEXT)
                    ?: intent.getStringExtra(Intent.EXTRA_TEXT)
                handleTextHtml(html)
            }

            "text/x-uri", "text/uri-list" -> {
                val text = intent.getStringExtra(Intent.EXTRA_TEXT)
                val uri = intent.parcelable<Uri>(Intent.EXTRA_STREAM)
                handleUriList(text, uri)
            }

            "application/xhtml+xml" -> {
                val html = intent.getStringExtra(Intent.EXTRA_TEXT)
                handleTextHtml(html)
            }

            "message/rfc822" -> {
                val body = intent.getStringExtra(Intent.EXTRA_TEXT)
                handleEmail(body)
            }

            else -> Timber.w( "Unsupported MIME type: ${intent.type}")
        }
    }

    private fun extractUrls(text: String): List<String> {
        val result = mutableListOf<String>()
        val matcher = Patterns.WEB_URL.matcher(text)
        while (matcher.find()) result.add(matcher.group())
        return result
    }

    private fun handleTextPlain(text: String) {
        //extractUrls(text).forEach { Timber.d("URL_PLAIN", it) }
        extractUrls(text).firstOrNull()?.let { startActivity(it) }
    }

    private fun handleTextHtml(html: String?) {
        if (html == null) return

        val hrefRegex = Regex("""href=["']([^"']+)["']""", RegexOption.IGNORE_CASE)
        val hrefUrls = hrefRegex.findAll(html).map { it.groupValues[1] }.toList()

        val renderedText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
        val plainUrls = extractUrls(renderedText)

//        (hrefUrls + plainUrls).distinct().forEach {
//            Timber.d("URL_HTML", it)
//        }
        (hrefUrls + plainUrls).firstOrNull()?.let { startActivity(it) }
    }

    private fun handleUriList(text: String?, uri: Uri?) {
        text?.lines()
            ?.map { it.trim() }
            ?.filter { Patterns.WEB_URL.matcher(it).find() }
            ?.forEach { Timber.d("URI_TEXT: $it") }

//        uri?.let { Timber.d("URI_STREAM", it.toString()) }
        uri?.toString()?.let { startActivity(it) }
    }

    private fun handleEmail(body: String?) {
        if (body == null) return
//        extractUrls(body).forEach { Timber.d("URL_EMAIL", it) }
        extractUrls(body).firstOrNull()?.let { startActivity(it) }
    }

    private fun handleMultipleUris(uris: List<Uri>) {
//        uris.forEach { Timber.d("URI_MULTI", it.toString()) }
        uris.firstOrNull()?.toString()?.let { startActivity(it) }
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
        ExperimentalPermissionsApi::class, ExperimentalTextApi::class,
        ExperimentalCoroutinesApi::class, FlowPreview::class
    )
    private fun startActivity(url: String) {
        Timber.d("URL: $url")

        moduleNavigation.buildIntent(this, ActivityLabel.SelectionActivity)?.apply {
            putExtra(EXTRA_SHARE_URL, url)
        }.also { intent ->
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_SHARE_URL = "extra_share_url"
    }
}
