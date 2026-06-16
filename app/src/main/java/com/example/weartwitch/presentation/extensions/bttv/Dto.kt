package com.example.weartwitch.presentation.extensions.bttv

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BttvUserResponse(
    @SerialName("id") val id: String,
    @SerialName("channelEmotes") val channelEmotes: List<BttvEmote>,
    @SerialName("sharedEmotes") val sharedEmotes: List<BttvEmote>
)

@Serializable
data class BttvEmote(
    @SerialName("id") val id: String,
    @SerialName("code") val code: String,
    @SerialName("imageType") val imageType: String,
    @SerialName("animated") val animated: Boolean = false
)