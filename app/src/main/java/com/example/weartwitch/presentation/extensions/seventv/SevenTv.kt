package com.example.weartwitch.presentation.extensions.seventv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import kotlinx.serialization.Serializable
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class GraphQlRequest(
    val query: String,
    val variables: Map<String, String> = emptyMap()
)

class SevenTvClient(
    private val client: OkHttpClient
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun global7tv(): List<SevenTvEmoteEntry> = withContext(Dispatchers.IO) {
        val query = $$"""
            query EmoteSet($id: ObjectID!) {
                emoteSet(id: $id) {
                    emotes {
                        name
                        id
                    }
                }
            }
        """.trimIndent()

        val body = GraphQlRequest(
            query = query,
            variables = mapOf("id" to "01HKQT8EWR000ESSWF3625XCS4")
        )

        val requestBody = json.encodeToString(body)
            .toRequestBody(
                "application/json".toMediaType()
            )

        val req = Request.Builder()
            .url("https://7tv.io/v3/gql")
            .post(requestBody)
            .build()

        try {
            client.newCall(req).execute().use { res ->
                if (!res.isSuccessful) throw IOException("Error fetching global 7TV emotes! Status: ${res.code}")
                val parsed = json.decodeFromString<SevenTvApiGlobalResponse>(res.body.string())
                parsed.data?.emoteSet?.emotes ?: emptyList()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getChannel7Tv(channel: String): List<SevenTvEmoteEntry> =
        withContext(Dispatchers.IO) {
            val query = $$"""
            query GetUserEmotes($username: String!) {
                users(query: $username) {
                    emote_sets {
                        emotes {
                            id
                            name
                        }
                    }
                }
            }
        """.trimIndent()

            val body = GraphQlRequest(
                query = query,
                variables = mapOf("username" to channel)
            )

            val requestBody = json.encodeToString(body)
                .toRequestBody(
                    "application/json".toMediaType()
                )

            val req = Request.Builder()
                .url("https://7tv.io/v3/gql")
                .post(requestBody)
                .build()

            try {
                client.newCall(req).execute().use { res ->
                    if (!res.isSuccessful) throw IOException("Error fetching channel 7TV emotes! Status: ${res.code}")
                    val parsed = json.decodeFromString<SevenTvChannelResponse>(res.body.string())
                    parsed.data?.users
                        ?.firstOrNull()
                        ?.emoteSets
                        ?.flatMap { it.emotes ?: emptyList() }
                        ?: emptyList()
                }
            } catch (e: Exception) {
                throw e
            }
        }
}