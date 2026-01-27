package no.emil.organizer.data.models

data class AppSettings(
    val theme: AppTheme,
    val todoDefaultDueTimeMinutes: Int,
    val todoNotifyMinutesBefore: Int,
    val showCompletedToday: Boolean
)

enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK
}