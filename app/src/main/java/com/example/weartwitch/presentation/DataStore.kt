package com.example.weartwitch.presentation

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")
val CHANNELS_KEY = stringSetPreferencesKey("channels")

fun getChannels(context: Context): Flow<Set<String>> {
    return context.dataStore.data.map { prefs ->
        prefs[CHANNELS_KEY] ?: emptySet()
    }
}

suspend fun readChannels(context: Context): Set<String> {
    val prefs = context.dataStore.data.first()
    return prefs[CHANNELS_KEY] ?: emptySet()
}

suspend fun saveChannel(context: Context, channel: String): Boolean {
    val existing = readChannels(context)
    if (channel in existing) return false

    context.dataStore.edit { prefs ->
        val current = prefs[CHANNELS_KEY] ?: emptySet()
        prefs[CHANNELS_KEY] = current + channel
    }
    Log.d("WearTwitch", "Saved channel: $channel")
    return true
}

suspend fun removeChannel(context: Context, channel: String): Boolean {
    val existing = readChannels(context)
    if (channel !in existing) return false

    context.dataStore.edit { prefs ->
        val current = prefs[CHANNELS_KEY] ?: emptySet()
        prefs[CHANNELS_KEY] = current - channel
    }
    Log.d("WearTwitch", "Channel Removed: $channel")
    return true
}
