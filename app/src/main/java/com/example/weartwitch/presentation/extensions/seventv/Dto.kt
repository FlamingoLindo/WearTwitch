package com.example.weartwitch.presentation.extensions.seventv

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class SevenTvChannelResponse(
    @SerialName("data") val data: SevenTvDataWrapper? = null,
    @SerialName("extensions") val extensions: SevenTvExtensions? = null
)

@Serializable
data class SevenTvDataWrapper(
    @SerialName("users") val users: List<SevenTvUserResult>? = null
)

@Serializable
data class SevenTvUserResult(
    @SerialName("emote_sets") val emoteSets: List<SevenTvEmoteSet>? = null
)

@Serializable
data class SevenTvEmoteSet(
    @SerialName("emotes") val emotes: List<SevenTvEmoteEntry>? = null
)

@Serializable
data class SevenTvEmoteEntry(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null
)

@Serializable
data class SevenTvExtensions(
    @SerialName("analyzer") val analyzer: SevenTvAnalyzer? = null
)

@Serializable
data class SevenTvAnalyzer(
    @SerialName("complexity") val complexity: Int? = null,
    @SerialName("depth") val depth: Int? = null
)

@Serializable
data class SevenTvApiGlobalResponse(
    @SerialName("data") val data: SevenTvData? = null
)

@Serializable
data class SevenTvData(
    @SerialName("emoteSet") val emoteSet: SevenTvEmoteSetGlobal? = null
)

@Serializable
data class SevenTvEmoteSetGlobal(
    @SerialName("emotes") val emotes: List<SevenTvEmoteEntry>? = null
)

@Serializable
data class SevenTvEmote(
    @SerialName("name") val name: String? = null,
    @SerialName("id") val id: String? = null
)