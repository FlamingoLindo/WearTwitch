package com.example.weartwitch.presentation.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import com.example.weartwitch.presentation.composables.ChannelNameHeader
import com.example.weartwitch.presentation.composables.messages.ChatMessage
import com.example.weartwitch.presentation.composables.messages.ChatMessages
import androidx.compose.runtime.getValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.runtime.setValue

@Composable
fun Channel(
    name: String,
    modifier: Modifier = Modifier,
    onSelectChannel: () -> Unit,
    messages: List<ChatMessage>,
    listState: ScalingLazyListState = rememberScalingLazyListState()
) {
    var showHeader by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    showHeader = !showHeader
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (showHeader) {
            ChannelNameHeader(name, onClick = onSelectChannel)
        }

        ChatMessages(
            messages = messages,
            modifier = Modifier.weight(1f),
            listState = listState
        )
    }
}