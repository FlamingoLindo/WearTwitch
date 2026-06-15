package com.example.weartwitch.presentation.composables.messages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Text
import coil.compose.AsyncImage

@Composable
fun MessageItem(message: ChatMessage) {
    val senderColor = remember(message.sender.color) {
        message.sender.color.toColor()
    }

    val (annotatedString, inlineContent) = remember(
        message.content,
        message.emotes,
        message.sender.username,
        senderColor
    ) {
        val text = buildAnnotatedString {
            // Sender name in its specific color
            pushStyle(SpanStyle(color = senderColor, fontWeight = FontWeight.Bold))
            append("${message.sender.username}: ")
            pop()

            // Message content with emotes
            if (message.emotes.isEmpty()) {
                append(message.content)
            } else {
                var remaining = message.content
                while (remaining.isNotEmpty()) {
                    val match = message.emotes.keys
                        .mapNotNull { word ->
                            val idx = remaining.indexOf(word)
                            if (idx >= 0) word to idx else null
                        }
                        .minByOrNull { it.second }

                    if (match == null) {
                        append(remaining)
                        break
                    }

                    val (word, _) = match
                    val idx = match.second
                    append(remaining.substring(0, idx))
                    appendInlineContent(word, word)
                    remaining = remaining.substring(idx + word.length)
                }
            }
        }

        val inlineMap = if (message.emotes.isEmpty()) {
            emptyMap()
        } else {
            message.emotes.map { (word, url) ->
                word to InlineTextContent(
                    placeholder = Placeholder(
                        width = 20.sp,
                        height = 20.sp,
                        PlaceholderVerticalAlign.Center
                    )
                ) {
                    AsyncImage(
                        model = url,
                        contentDescription = word,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }.toMap()
        }

        text to inlineMap
    }

    Text(
        text = annotatedString,
        inlineContent = inlineContent,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}
