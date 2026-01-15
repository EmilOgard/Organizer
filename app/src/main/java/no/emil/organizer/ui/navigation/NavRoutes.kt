package no.emil.organizer.ui.navigation

sealed class NavRoute(val route: String) {
    object Notes : NavRoute("notes")
    object Todo : NavRoute("todo")
    object Finance : NavRoute("finance")
    object Calendar : NavRoute("calendar")
    object Settings : NavRoute("settings")
    object AddNote : NavRoute("add_note/{noteId}") {
        fun createRoute(noteId: Long? = null) = "add_note/${noteId ?: -1}"
    }

}