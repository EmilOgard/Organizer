package no.emil.organizer.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import no.emil.organizer.OrganizerApp
import no.emil.organizer.data.repositories.NoteRepository
import no.emil.organizer.data.repositories.TodoRepository
import no.emil.organizer.ui.screens.AddNoteScreen
import no.emil.organizer.ui.screens.CalendarScreen
import no.emil.organizer.ui.screens.FinanceScreen
import no.emil.organizer.ui.screens.NotesScreen
import no.emil.organizer.ui.screens.SettingsScreen
import no.emil.organizer.ui.screens.TodoScreen
import no.emil.organizer.viewmodels.NoteViewModel
import no.emil.organizer.viewmodels.TodoViewModel

@Composable
fun AppNavGraph(navController: NavHostController, padding: PaddingValues) {

    val context = LocalContext.current
    val app = context.applicationContext as OrganizerApp

    val noteRepository = NoteRepository(app.db.noteDao())
    val noteViewModel = remember {
        NoteViewModel(noteRepository)
    }

    val todoRepository = TodoRepository(app.db.todoItemDao(), app.db.todoInstanceDao())
    val todoViewModel = remember {
        TodoViewModel(todoRepository)
    }

    NavHost(
        navController = navController,
        startDestination = NavRoute.Notes.route,
        modifier = Modifier.padding(padding)
    ) {
        composable(NavRoute.Notes.route) { NotesScreen(viewModel = noteViewModel, navController) }
        composable(NavRoute.Todo.route) { TodoScreen(viewModel = todoViewModel) }
        composable(NavRoute.Finance.route) { FinanceScreen() }
        composable(NavRoute.Calendar.route) { CalendarScreen() }
        composable(NavRoute.Settings.route) { SettingsScreen() }
        composable(
            NavRoute.AddNote.route,
            arguments = listOf(navArgument("noteId") { defaultValue = -1L })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1L
            AddNoteScreen(
                viewModel = noteViewModel,
                navController = navController,
                noteId = if (noteId != -1L) noteId else null,
                onNoteSaved = { navController.popBackStack() }
            )
        }
    }
}