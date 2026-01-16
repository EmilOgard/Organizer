package no.emil.organizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import no.emil.organizer.data.database.OrganizerDatabase
import no.emil.organizer.data.repositories.TodoRepository
import no.emil.organizer.ui.navigation.AppNavGraph
import no.emil.organizer.ui.navigation.BottomBar
import no.emil.organizer.ui.theme.OrganizerTheme
import no.emil.organizer.viewmodels.TodoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            OrganizerDatabase::class.java,
            "organizer_db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val todoRepo = TodoRepository(db.todoItemDao(), db.todoInstanceDao())
        val todoViewModel = TodoViewModel(todoRepo)

        setContent {
            val navController = rememberNavController()
            OrganizerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomBar(navController) }
                ) { innerPadding ->
                    AppNavGraph(
                        navController = navController,
                        padding = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun Welcome(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "App: $name",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun WelcomePreview() {
    OrganizerTheme {
        Welcome("Organizer")
    }
}