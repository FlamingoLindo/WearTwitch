package com.example.weartwitch.presentation.composables

import android.app.RemoteInput
import android.content.Context
import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

val Context.dataStore by preferencesDataStore(name = "settings")
val CHANNELS_KEY = stringSetPreferencesKey("channels")

suspend fun readChannels(context: Context): Set<String> {
    val prefs = context.dataStore.data.first()
    return prefs[CHANNELS_KEY] ?: emptySet()
}

suspend fun saveChannel(context: Context, channel: String): Boolean {
    val existing = readChannels(context)
    if (channel in existing) return false

    context.dataStore.edit { prefs ->
        val current = prefs[CHANNELS_KEY] ?: emptySet()
        prefs[CHANNELS_KEY] = current + channel
    }
    Log.d("WearTwitch", "Saved channel: $channel")
    return true
}

@Composable
fun AddChannel() {
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
            scope.launch { saveChannel(context, channel) }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(if (channelName.isEmpty()) "Enter channel name" else "Added: $channelName")

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val intent = RemoteInputIntentHelper.createActionRemoteInputIntent()
                val remoteInput = RemoteInput.Builder("channel_name")
                    .setLabel("Channel name")
                    .wearableExtender { setEmojisAllowed(false) }
                    .build()
                RemoteInputIntentHelper.putRemoteInputsExtra(intent, listOf(remoteInput))
                launcher.launch(intent)
            },
            label = { Text("Type channel name") }
        )

//        Button(
//            onClick = {
//                scope.launch {
//                    val added = saveChannel(context, "flamingo_lindo")
//
//                    if (added) {
//                        channelName = "flamingo_lindo"
//                    }
//                }
//            },
//            label = {
//                Text("Add Flamingo")
//            }
//        )
    }
}