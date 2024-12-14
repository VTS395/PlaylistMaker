package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private val DARK_THEME_PREFS = "darkThemePrefs"
    private val DARK_THEME_KEY = "darkThemeKey"
    var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(DARK_THEME_PREFS, Context.MODE_PRIVATE)

        darkTheme = getSavedThemePreference()

        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        saveThemePreference(darkThemeEnabled)

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun saveThemePreference(isDarkTheme: Boolean) {
        sharedPreferences.edit()
        .putBoolean(DARK_THEME_KEY, isDarkTheme)
        .apply()
    }

    private fun getSavedThemePreference(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }
}

