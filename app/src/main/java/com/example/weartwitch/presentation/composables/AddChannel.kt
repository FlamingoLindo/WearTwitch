package com.example.weartwitch.presentation.composables

import android.app.RemoteInput
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import com.example.weartwitch.R
import com.example.weartwitch.presentation.saveChannel
import kotlinx.coroutines.launch

@Composable
fun AddChannel(onChannelAdd: (String) -> Unit) {
    var channelName by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data ?: return@rememberLauncherForActivityResult
        val results = RemoteInput.getResultsFromIntent(data)
        val channel = results?.getCharSequence("channel_name")
            ?.toString()?.trim()?.lowercase() ?: return@rememberLauncherForActivityResult

        if (channel.isNotEmpty()) {
            channelName = channel
            scope.launch {
                saveChannel(context, channel)
                onChannelAdd(channel)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.channel_name))

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val intent = RemoteInputIntentHelper.createActionRemoteInputIntent()
                val remoteInput = RemoteInput.Builder("channel_name")
                    .setLabel("Input channel name")
                    .wearableExtender { setEmojisAllowed(false) }
                    .build()
                RemoteInputIntentHelper.putRemoteInputsExtra(intent, listOf(remoteInput))
                launcher.launch(intent)
            },
            label = { Text(stringResource(R.string.input_channel_name)) }
        )
    }
}