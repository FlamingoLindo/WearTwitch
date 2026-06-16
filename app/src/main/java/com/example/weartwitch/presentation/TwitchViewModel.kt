package com.example.weartwitch.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weartwitch.presentation.composables.messages.ChatMessage
import com.example.weartwitch.presentation.extensions.bttv.BttvClient
import com.example.weartwitch.presentation.extensions.ffz.FfzClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel

class TwitchViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _bttvEmotes = MutableStateFlow<Map<String, String>>(emptyMap())

    private val _ffzEmotes = MutableStateFlow<Map<String, String>>(emptyMap())

    private var client: TwitchClient? = null
    private val messageBuffer = Channel<ChatMessage>(Channel.UNLIMITED)

    init {
        viewModelScope.launch {
            val currentBatch = mutableListOf<ChatMessage>()
            while (true) {
                val msg = messageBuffer.receive()
                currentBatch.add(msg)

                var next = messageBuffer.tryReceive().getOrNull()
                while (next != null) {
                    currentBatch.add(next)
                    if (currentBatch.size > 20) break
                    next = messageBuffer.tryReceive().getOrNull()
                }

                _messages.value = (_messages.value + currentBatch).takeLast(50)
                currentBatch.clear()
            }
        }
    }

    fun connect(channel: String) {
        client?.disconnect()
        _messages.value = emptyList()
        _bttvEmotes.value = emptyMap() // clear stale emotes from previous channel
        _ffzEmotes.value = emptyMap()

        viewModelScope.launch {
            try {
                val bttvApi = BttvClient(AppHttpClient.instance)
                val global = bttvApi.globalBttv()
                val channelData = bttvApi.getChannelBttv(channel)

                val merged = buildMap {
                    for (e in global) {
                        put(e.code, "https://cdn.betterttv.net/emote/${e.id}/3x")
                    }
                    for (e in channelData.channelEmotes + channelData.sharedEmotes) {
                        put(e.code, "https://cdn.betterttv.net/emote/${e.id}/3x")
                    }
                }
                _bttvEmotes.value = merged
            } catch (e: Exception) {
                Log.e("WearTwitch", "BTTV fetch failed", e)
            }

            try {
                val ffzApi = FfzClient(AppHttpClient.instance)
                val global = ffzApi.globalFfz()
                val channelData = ffzApi.getChannelFfz(channel)

                val merged = buildMap{
                    for (e in global + channelData) {
                        val name = e.name ?: continue
                        val url = e.urls?.values?.lastOrNull() ?: continue
                        put(name, url)
                    }
                }
                _ffzEmotes.value = merged
            } catch (e: Exception) {
                Log.e("WearTwitch", "FFZ fetch failed", e)
            }
        }

        // Pass lambdas so TwitchClient always reads the latest emote maps
        client = TwitchClient(
            channel = channel,
            bttvEmotes = { _bttvEmotes.value },
            ffzemotes = { _ffzEmotes.value },
            onMessage = { message -> messageBuffer.trySend(message) }
        )
        client?.connect()
    }

    fun disconnect() = client?.disconnect()

    override fun onCleared() {
        super.onCleared()
        client?.disconnect()
    }
}