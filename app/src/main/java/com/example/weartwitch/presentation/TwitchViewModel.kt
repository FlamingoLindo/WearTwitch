package com.example.weartwitch.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weartwitch.presentation.composables.messages.ChatMessage
import com.example.weartwitch.presentation.extensions.bttv.BttvClient
import com.example.weartwitch.presentation.extensions.ffz.FfzClient
import com.example.weartwitch.presentation.extensions.seventv.SevenTvClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.Job

enum class ChatState {
    Idle,
    LoadingEmotes,
    Connected,
    Error
}

class TwitchViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _state = MutableStateFlow<ChatState>(ChatState.Idle)
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _bttvEmotes = MutableStateFlow<Map<String, String>>(emptyMap())

    private val _ffzEmotes = MutableStateFlow<Map<String, String>>(emptyMap())

    private val _sevenEmotes = MutableStateFlow<Map<String, String>>(emptyMap())

    private var client: TwitchClient? = null
    private var connectJob: Job? = null
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
        connectJob?.cancel()
        client?.disconnect()
        _messages.value = emptyList()
        _bttvEmotes.value = emptyMap()
        _ffzEmotes.value = emptyMap()
        _sevenEmotes.value = emptyMap()
        _state.value = ChatState.LoadingEmotes

        connectJob = viewModelScope.launch {
            val jobs = listOf(
                launch { fetchBttv(channel) },
                launch { fetchFfz(channel) },
                launch { fetchSevenTv(channel) }
            )
            jobs.joinAll()

            // Pass lambdas so TwitchClient always reads the latest emote maps
            client = TwitchClient(
                channel = channel,
                bttvEmotes = { _bttvEmotes.value },
                ffzEmotes = { _ffzEmotes.value },
                sevenTvEmotes = { _sevenEmotes.value },
                onMessage = { message -> messageBuffer.trySend(message) }
            )
            client?.connect()
            _state.value = ChatState.Connected
        }
    }

    private suspend fun fetchBttv(channel: String) {
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
    }

    private suspend fun fetchFfz(channel: String) {
        try {
            val ffzApi = FfzClient(AppHttpClient.instance)
            val global = ffzApi.globalFfz()
            val channelData = ffzApi.getChannelFfz(channel)

            val merged = buildMap {
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

    private suspend fun fetchSevenTv(channel: String) {
        try {
            val sevenTvApi = SevenTvClient(AppHttpClient.instance)
            val global = sevenTvApi.global7tv()
            val channelData = sevenTvApi.getChannel7Tv(channel)

            val merged = buildMap {
                for (e in global + channelData) {
                    val name = e.name ?: continue
                    val id = e.id ?: continue
                    put(name, "https://cdn.7tv.app/emote/$id/3x.webp")
                }
            }
            _sevenEmotes.value = merged
        } catch (e: Exception) {
            Log.e("WearTwitch", "7TV fetch failed", e)
        }
    }

    override fun onCleared() {
        super.onCleared()
        client?.disconnect()
    }
}