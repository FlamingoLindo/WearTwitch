package com.example.weartwitch.presentation.extensions.bttv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

class BttvClient(
    private val client: OkHttpClient
) {
    private val json = Json { ignoreUnknownKeys = true }
    suspend fun globalBttv(): List<BttvEmote> = withContext(Dispatchers.IO) {
        val req = Request.Builder().url("https://api.betterttv.net/3/cached/emotes/global").build()

        try {
            client.newCall(req).execute().use { res ->
                if (!res.isSuccessful) {
                    val errorMsg = "Error while fetching global BTTV emotes! Status: ${res.code}"
                    throw IOException(errorMsg)
                }
                json.decodeFromString<List<BttvEmote>>(res.body.string())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getChannelId(channel: String): String = withContext(Dispatchers.IO) {
        val req = Request.Builder().url("https://decapi.me/twitch/id/$channel").build()

        try {
            client.newCall(req).execute().use { res ->
                if (!res.isSuccessful) {
                    val errorMsg = "Error while fetching channel ID! Status: ${res.code}"
                    throw IOException(errorMsg)
                }
                res.body.string().trim()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getChannelBttv(channel: String): BttvUserResponse = withContext(Dispatchers.IO) {
        val channelId = getChannelId(channel)
        val req =
            Request.Builder().url("https://api.betterttv.net/3/cached/users/twitch/$channelId")
                .build()

        try {
            client.newCall(req).execute().use { res ->
                if (!res.isSuccessful) {
                    val errorMsg = "Error while fetching channel BTTV emotes! Status: ${res.code}"
                    throw IOException(errorMsg)
                }
                json.decodeFromString<BttvUserResponse>(res.body.string())
            }
        } catch (e: Exception) {
            throw e
        }
    }
}