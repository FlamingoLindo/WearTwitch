package com.example.weartwitch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weartwitch.presentation.composables.messages.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel

class TwitchViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private var client: TwitchClient? = null
    private val messageBuffer = Channel<ChatMessage>(Channel.UNLIMITED)

    init {
        // Process messages in batches to reduce UI recompositions
        viewModelScope.launch {
            val currentBatch = mutableListOf<ChatMessage>()
            while (true) {
                val msg = messageBuffer.receive()
                currentBatch.add(msg)
                
                // If there are more messages immediately available, keep collecting
                var next = messageBuffer.tryReceive().getOrNull()
                while (next != null) {
                    currentBatch.add(next)
                    if (currentBatch.size > 20) break // Don't batch too many at once
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
        client = TwitchClient(
            channel = channel,
            onMessage = { message ->
                messageBuffer.trySend(message)
            }
        )
        client?.connect()
    }

    fun disconnect() = client?.disconnect()

    override fun onCleared() {
        super.onCleared()
        client?.disconnect()
    }
}