package no.emil.organizer.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import no.emil.organizer.data.models.AppSettings
import no.emil.organizer.data.models.AppTheme

class SettingsRepository(private val dataStore: DataStore<Preferences>) {
    val settings: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            theme = AppTheme.valueOf(
                prefs[SettingsKeys.THEME] ?: AppTheme.SYSTEM.name
            ),
            todoDefaultDueTimeMinutes =
                prefs[SettingsKeys.DEFAULT_DUE_MINUTES] ?: 1439,
            todoNotifyMinutesBefore =
                prefs[SettingsKeys.DEFAULT_DUE_MINUTES] ?: 30,
            showCompletedToday =
                prefs[SettingsKeys.SHOW_COMPLETED_TODAY] ?: true
                
        )
    }

    suspend fun setTheme(theme: AppTheme) {
        dataStore.edit {
            it[SettingsKeys.THEME] = theme.name
        }
    }
    suspend fun setNotifyMinutes(minutes: Int) {
        dataStore.edit {
            it[SettingsKeys.NOTIFY_BEFORE] = minutes
        }
    }
    suspend fun setShowCompletedToday(value: Boolean) {
        dataStore.edit {
            it[SettingsKeys.SHOW_COMPLETED_TODAY] = value
        }
    }
}