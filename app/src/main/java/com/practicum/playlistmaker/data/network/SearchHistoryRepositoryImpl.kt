package com.practicum.playlistmaker.data.network

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.domain.models.Track

class SearchHistoryRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SearchHistoryRepository {
    companion object {
        private const val HISTORY_KEY = "history"
        private const val MAX_HISTORY_SIZE = 10
    }

    private val gson = Gson()

    override fun addTrackToHistory(track: Track) {
        val currentHistory = getSearchHistory().toMutableList()
        if (currentHistory.contains(track)) {
            currentHistory.remove(track)
        }
        currentHistory.add(0, track)

        if (currentHistory.size > MAX_HISTORY_SIZE) {
            currentHistory.removeAt(currentHistory.lastIndex)
        }
        saveHistory(currentHistory)
    }

    override fun getSearchHistory(): List<Track> {
        val historyJson = sharedPreferences.getString(HISTORY_KEY, null)
        return if (historyJson != null) {
            gson.fromJson(historyJson, Array<Track>::class.java).toList()
        } else {
            emptyList()
        }
    }

    override fun clearHistory() {
        saveHistory(emptyList())
    }

    private fun saveHistory(history: List<Track>) {
        val historyJson = gson.toJson(history)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, historyJson)
            .apply()
    }
}