package com.example.cryptowallet.Classes

import android.content.Context

class SharedPrefsHelper(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val SELECTED_COINS_KEY = "selected_coins_key"
    }

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setSelectedCoinIds(selectedCoinIds: Set<Long>) {
        prefs.edit().putStringSet(SELECTED_COINS_KEY, selectedCoinIds.map { it.toString() }.toSet()).apply()
    }

    fun getSelectedCoinIds(): Set<Long> {
        val selectedCoinsStrings = prefs.getStringSet(SELECTED_COINS_KEY, setOf()) ?: setOf()
        return selectedCoinsStrings.mapNotNull { it.toLongOrNull() }.toSet()
    }
}
