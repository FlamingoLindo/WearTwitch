package com.example.weartwitch.presentation.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weartwitch.presentation.composables.ChannelNameHeader
import com.example.weartwitch.presentation.composables.ChatMessage
import com.example.weartwitch.presentation.composables.ChatMessages
import com.example.weartwitch.presentation.composables.Sender

@Composable
fun Channel(
    name: String,
    modifier: Modifier = Modifier,
    onSelectChannel: () -> Unit,
) {
    val mockMessages = listOf(
        ChatMessage(
            sender = Sender(
                username = "flamingo",
                color = "#FF69B4"
            ),
            content = "Hello chat!",
        ),
        ChatMessage(
            sender = Sender(
                username = "viewer123",
                color = "#00FF00"
            ),
            content = "Nice project 👀",
        ),
        ChatMessage(
            sender = Sender(
                username = "rustacean",
                color = "#1E90FF"
            ),
            content = "Turn the lights blue!",
        ),
        ChatMessage(
            sender = Sender(
                username = "test",
                color = "#1E90FF"
            ),
            content = "Wow!",
        ),
        ChatMessage(
            sender = Sender(
                username = "test2",
                color = "#1E90FF"
            ),
            content = "LuL this streamer is so funny, cant wait to see how he does at the end of the game!",
        ),
        ChatMessage(
            sender = Sender(
                username = "test3",
                color = "#1E90FF"
            ),
            content = "GG WP",
        )
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ChannelNameHeader(name, onClick = onSelectChannel)

        ChatMessages(
            messages = mockMessages,
            modifier = Modifier.weight(1f)
        )
    }
}