package no.emil.organizer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.emil.organizer.data.models.AppSettings
import no.emil.organizer.data.models.AppTheme
import no.emil.organizer.data.repositories.SettingsRepository

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {
    val settings = repository.settings.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AppSettings(
            theme = AppTheme.SYSTEM,
            todoDefaultDueTimeMinutes = 1439,
            todoNotifyMinutesBefore = 30,
            showCompletedToday = true
        )
    )

    fun setTheme(theme: AppTheme) =
        viewModelScope.launch { repository.setTheme(theme) }

    fun setNotifyMinutes(minutes: Int) =
        viewModelScope.launch { repository.setNotifyMinutes(minutes) }

    fun toggleShowCompletedToday(value: Boolean) =
        viewModelScope.launch { repository.setShowCompletedToday(value) }
}