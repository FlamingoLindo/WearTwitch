package com.example.weartwitch.presentation.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Icon
import com.example.weartwitch.R

@Composable
fun SelectChannel(
    channels: Set<String>,
    onAddChannel: () -> Unit,
    onSelectChannel: (String) -> Unit
) {
    Log.d("WearTwitch", channels.toString())
    val listState = rememberScalingLazyListState()

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(), state = listState
    ) {
        items(channels.count()) { i ->
            val channelName = channels.elementAt(i)
            Chip(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                onClick = { onSelectChannel(channelName) },
                colors = ChipDefaults.chipColors(
                    Color.DarkGray,
                    Color.White
                ),
                label = {
                    Text(
                        text = channelName,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp
                    )
                })
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
                    androidx.wear.compose.material3.Text(stringResource(R.string.add_channel))
                }
            )
        }
    }
}