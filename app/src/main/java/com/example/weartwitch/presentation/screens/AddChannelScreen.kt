package com.example.weartwitch.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material3.Text
import com.example.weartwitch.R
import com.example.weartwitch.presentation.composables.AddChannel

@Composable
fun AddChannelScreen() {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "ADD CHANNEL SCREEN",
//            modifier = Modifier.fillMaxWidth(0.8f),
//            maxLines = 2,
//            textAlign = TextAlign.Center,
//            fontWeight = FontWeight.Bold
//        )
//    }
    AddChannel()
}