package com.example.weartwitch.presentation.screens

import androidx.compose.runtime.Composable
import com.example.weartwitch.presentation.composables.AddChannel

@Composable
fun AddChannelScreen(onChannelAdd: (String) -> Unit) {
    AddChannel(onChannelAdd = onChannelAdd)
}