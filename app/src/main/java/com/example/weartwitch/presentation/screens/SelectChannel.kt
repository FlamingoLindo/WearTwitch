package com.example.weartwitch.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.compose.foundation.layout.Row
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButton
import androidx.wear.compose.material3.Text
import com.example.weartwitch.R
import com.example.weartwitch.presentation.removeChannel
import kotlinx.coroutines.launch

@Composable
fun SelectChannel(
    channels: Set<String>,
    onAddChannel: () -> Unit,
    onSelectChannel: (String) -> Unit,
    listState: ScalingLazyListState = rememberScalingLazyListState()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState
    ) {
        items(channels.count()) { i ->
            val channelName = channels.elementAt(i)
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectChannel(channelName) },
                    label = {
                        Text(
                            text = channelName,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                )
                IconButton(onClick = {
                    scope.launch {
                        removeChannel(context, channelName)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete_cha_ico)
                    )
                }
            }
        }
        item {
            Button(
                onClick = onAddChannel,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_channel_content_description)
                    )
                },
                label = {
                    Text(stringResource(R.string.add_channel))
                }
            )
        }
    }
}
