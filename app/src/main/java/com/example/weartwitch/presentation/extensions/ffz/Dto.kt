package com.example.weartwitch.presentation.extensions.ffz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FfzEmote(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("urls") val urls: Map<String, String>? = null
)

@Serializable
data class FfzEmoteSet(
    @SerialName("id") val id: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("emoticons") val emoticons: List<FfzEmote>? = null
)

@Serializable
data class FfzGlobalResponse(
    @SerialName("default_sets") val defaultSets: List<Int>? = null,
    @SerialName("sets") val sets: Map<String, FfzEmoteSet>? = null
)

@Serializable
data class FfzChannelResponse(
    @SerialName("room") val room: FfzRoom? = null,
    @SerialName("sets") val sets: Map<String, FfzEmoteSet>? = null
)

@Serializable
data class FfzRoom(
    @SerialName("id") val id: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("set") val set: Int? = null
)