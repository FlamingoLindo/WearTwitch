package com.example.weartwitch.presentation.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.wear.compose.material.Text

data class Sender(
    val username: String,
    val color: String
)

data class ChatMessage(
    val content: String,
    val sender: Sender
)

fun String.toColor(): Color {
    return Color(this.toColorInt())
}

@Composable
fun ChatMessages(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 8.dp
        )
    ) {
        items(
            items = messages,
            key = { message ->
                "${message.sender.username}-${message.content}"
            }
        ) { message ->

            Column(
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(
                    text = "${message.sender.username}:",
                    color = message.sender.color.toColor()
                )

                Text(
                    text = message.content
                )
            }
        }
    }
}