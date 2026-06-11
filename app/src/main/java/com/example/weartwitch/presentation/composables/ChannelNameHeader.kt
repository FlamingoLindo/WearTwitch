package com.example.weartwitch.presentation.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Text

@Composable
fun ChannelNameHeader(
    name: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    var size = 10.sp
    if (name.length > 14) {
        size = 6.sp
    }
    Column(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(top = 8.dp)
            .clickable { onClick() },
    ) {
        Text(
            text = name,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 6.dp, vertical = 4.dp),
            fontSize = size,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
    }
}