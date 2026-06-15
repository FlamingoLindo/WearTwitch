package com.example.weartwitch.presentation

import com.example.weartwitch.presentation.composables.messages.ChatMessage
import com.example.weartwitch.presentation.composables.messages.Sender
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class TwitchClient(
    private val channel: String,
    private val onMessage: (ChatMessage) -> Unit
) {
    private val client = OkHttpClient.Builder()
        .pingInterval(4, TimeUnit.MINUTES)
        .build()

    private var ws: WebSocket? = null

    fun connect() {
        val req = Request.Builder()
            .url("wss://irc-ws.chat.twitch.tv:443")
            .build()

        ws = client.newWebSocket(req, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send("CAP REQ :twitch.tv/tags twitch.tv/commands")
                webSocket.send("PASS SCHMOOPIIE")
                webSocket.send("NICK justinfan${(10000..99999).random()}")
                webSocket.send("JOIN #$channel")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                if (text.startsWith("PING")) {
                    webSocket.send("PONG :tmi.twitch.tv")
                    return
                }
                text.lines().forEach { line ->
                    parseTwitchIrcLine(line)?.let(onMessage)
                }
            }
        })
    }

    fun disconnect() = ws?.close(1000, null)
}

fun parseTwitchIrcLine(raw: String): ChatMessage? {
    if (!raw.contains("PRIVMSG")) return null

    val tagSection = if (raw.startsWith("@")) raw.substringAfter("@").substringBefore(" :") else ""
    val tags = tagSection.split(";").associate {
        val parts = it.split("=", limit = 2)
        parts[0] to (parts.getOrElse(1) { "" })
    }

    val username = tags["display-name"] ?: return null
    val color = tags["color"].takeIf { !it.isNullOrBlank() }
        ?: "#${(username.hashCode() and 0xFFFFFF).toString(16).padStart(6, '0')}"
    val content = raw.substringAfterLast(" :").trim()

    val emoteMap = mutableMapOf<String, String>()
    val emotesTag = tags["emotes"] ?: ""

    if (emotesTag.isNotEmpty()) {
        emotesTag.split("/").forEach { entry ->
            val parts = entry.split(":", limit = 2)
            if (parts.size < 2) return@forEach
            val emoteId = parts[0]
            val firstRange = parts[1].split(",")[0]
            val rangeParts = firstRange.split("-")
            if (rangeParts.size < 2) return@forEach
            val start = rangeParts[0].toIntOrNull() ?: return@forEach
            val end = rangeParts[1].toIntOrNull() ?: return@forEach
            if (end < content.length) {
                val emoteName = content.substring(start, end + 1)
                emoteMap[emoteName] =
                    "https://static-cdn.jtvnw.net/emoticons/v2/$emoteId/default/dark/3.0"
            }
        }
    }

    return ChatMessage(
        sender = Sender(username = username, color = color),
        content = content,
        emotes = emoteMap
    )
}