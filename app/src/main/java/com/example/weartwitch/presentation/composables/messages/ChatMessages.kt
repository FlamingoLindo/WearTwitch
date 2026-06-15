package com.example.weartwitch.presentation.composables.messages

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import java.util.UUID

data class Sender(
    val username: String,
    val color: String
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val emotes: Map<String, String> = emptyMap(),
    val sender: Sender
)

fun String.toColor(): Color {
    return try {
        Color(this.toColorInt())
    } catch (e: Exception) {
        Color.White
    }
}

@Composable
fun ChatMessages(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier,
    listState: ScalingLazyListState = rememberScalingLazyListState()
) {
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) return@derivedStateOf true
            
            val lastVisible = visibleItems.last().index
            lastVisible >= layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(messages.size) {
        if (isAtBottom && messages.isNotEmpty()) {
            listState.scrollToItem(messages.size - 1)
        }
    }

    ScalingLazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        autoCentering = null,
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 40.dp
        )
    ) {
        items(
            items = messages,
            key = { message -> message.id }
        ) { message ->
            MessageItem(message)
        }
    }
}

