package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.api.Track

class SearchHistory(private val sharedPreferences: SharedPreferences) {
    companion object {
        private const val HISTORY_KEY = "history"
        private const val MAX_HISTORY_SIZE = 10
    }

    private val gson = Gson()

    fun addTrackToHistory(track: Track) {
        val currentHistory = getSearchHistory()
        if (currentHistory.contains(track)) {
            currentHistory.remove(track)
        }
        currentHistory.add(0, track)

        if (currentHistory.size > MAX_HISTORY_SIZE) {
            currentHistory.removeAt(currentHistory.lastIndex)
        }
        saveHistory(currentHistory)
    }

    fun getSearchHistory(): MutableList<Track> {
        val historyJson = sharedPreferences.getString(HISTORY_KEY, null)
        return if (historyJson != null) {
            gson.fromJson(historyJson, Array<Track>::class.java).toMutableList()
        } else {
            mutableListOf()
        }
    }

    fun clearHistory() {
        saveHistory(mutableListOf())
    }

    private fun saveHistory(history: MutableList<Track>) {
        val historyJson = gson.toJson(history)
        sharedPreferences.edit()
            .putString(HISTORY_KEY, historyJson)
            .apply()
    }
}