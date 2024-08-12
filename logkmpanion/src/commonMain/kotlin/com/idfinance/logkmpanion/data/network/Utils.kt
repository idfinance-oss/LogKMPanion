package com.idfinance.logkmpanion.data.network

import com.idfinance.logkmpanion.domain.handleRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.ContentType
import io.ktor.http.Cookie
import io.ktor.http.Headers
import io.ktor.http.HttpStatusCode
import io.ktor.http.charset
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.http.cookies
import io.ktor.util.AttributeKey
import io.ktor.util.copyToBoth
import io.ktor.utils.io.ByteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.close
import io.ktor.utils.io.core.readText
import io.ktor.utils.io.writeFully
import io.ktor.utils.io.writer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class LoggedContent(
    private val originalContent: OutgoingContent,
    private val channel: ByteReadChannel
) : OutgoingContent.ReadChannelContent() {

    override val contentType: ContentType? = originalContent.contentType
    override val contentLength: Long? = originalContent.contentLength
    override val status: HttpStatusCode? = originalContent.status
    override val headers: Headers = originalContent.headers

    override fun <T : Any> getProperty(key: AttributeKey<T>): T? = originalContent.getProperty(key)

    override fun <T : Any> setProperty(key: AttributeKey<T>, value: T?) =
        originalContent.setProperty(key, value)

    override fun readFrom(): ByteReadChannel = channel
}

@OptIn(DelicateCoroutinesApi::class)
private fun OutgoingContent.WriteChannelContent.toReadChannel(): ByteReadChannel =
    GlobalScope.writer(Dispatchers.Default) {
        writeTo(channel)
    }.channel

@Suppress("DEPRECATION")
internal suspend fun OutgoingContent.observe(log: ByteWriteChannel): OutgoingContent = when (this) {
    is OutgoingContent.ByteArrayContent -> {
        log.writeFully(bytes())
        log.close()
        this
    }

    is OutgoingContent.ReadChannelContent -> {
        val responseChannel = ByteChannel()
        val content = readFrom()

        content.copyToBoth(log, responseChannel)
        LoggedContent(this, responseChannel)
    }

    is OutgoingContent.WriteChannelContent -> {
        val responseChannel = ByteChannel()
        val content = toReadChannel()
        content.copyToBoth(log, responseChannel)
        LoggedContent(this, responseChannel)
    }

    else -> {
        log.close()
        this
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Suppress("DEPRECATION")
internal suspend fun logRequest(sessionId: String, request: HttpRequestBuilder) {
    val content = request.body as OutgoingContent

    val url = request.url.toString()
    val requestId = request.attributes[requestIdKey]
    val method = request.method.value
    val headers = request.headers.entries().map { "${it.key}=${it.value.joinToString()}" }
    val cookies = request.cookies().map(Cookie::toString)


    if (request.contentType() == ContentType.Application.Json) {
        val bodyLog = StringBuilder()

        val charset = content.contentType?.charset() ?: Charsets.UTF_8

        val channel = ByteChannel()
        GlobalScope.launch(Dispatchers.Unconfined) {
            val text = channel.tryReadText(charset) ?: "[request body omitted]"
            bodyLog.appendLine(text)
        }.invokeOnCompletion {
            handleRequest(sessionId, requestId, url, method, headers, cookies, bodyLog.toString())
        }

        content.observe(channel)
    } else {
        handleRequest(sessionId, requestId, url, method, headers, cookies, "[request body omitted]")
    }
}

internal suspend inline fun ByteReadChannel.tryReadText(charset: Charset): String? = runCatching {
    readRemaining().readText(charset = charset)
}.getOrElse { null }