package no.emil.organizer.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.settingsDataStore by preferencesDataStore(name = "settings")

object SettingsKeys {
    val THEME = stringPreferencesKey("theme")
    val DEFAULT_DUE_MINUTES = intPreferencesKey("default_due_minutes")
    val NOTIFY_BEFORE = intPreferencesKey("notify_before")
    val SHOW_COMPLETED_TODAY = booleanPreferencesKey("show_completed_today")
}
