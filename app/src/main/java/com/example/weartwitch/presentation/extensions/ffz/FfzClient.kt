package com.example.weartwitch.presentation.extensions.ffz

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class FfzClient(
    private val client: OkHttpClient
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun globalFfz(): List<FfzEmote> = withContext(Dispatchers.IO) {
        val req = Request.Builder().url("https://api.frankerfacez.com/v1/set/global").build()

        try {
            client.newCall(req).execute().use { res ->
                if (!res.isSuccessful) throw IOException("Error fetching global FFZ emotes! Status: ${res.code}")
                val response = json.decodeFromString<FfzGlobalResponse>(res.body.string())
                response.sets?.values
                    ?.flatMap { it.emoticons ?: emptyList() }
                    ?: emptyList()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getChannelFfz(channel: String): List<FfzEmote> = withContext(Dispatchers.IO) {
        val req =
            Request.Builder().url("https://api.frankerfacez.com/v1/room/$channel")
                .build()

        try {
            client.newCall(req).execute().use { res ->
                if (!res.isSuccessful) throw IOException("Error fetching channel FFZ emotes! Status: ${res.code}")
                val response = json.decodeFromString<FfzChannelResponse>(res.body.string())
                response.sets?.values
                    ?.flatMap { it.emoticons ?: emptyList() }
                    ?: emptyList()
            }
        } catch (e: Exception) {
            throw e
        }
    }
}