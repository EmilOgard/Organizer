package no.emil.organizer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.emil.organizer.data.models.Note
import no.emil.organizer.ui.navigation.NavRoute
import no.emil.organizer.viewmodels.NoteViewModel


@Composable
fun NotesScreen(viewModel: NoteViewModel, navController: NavController) {
    val notes by viewModel.notes.collectAsState(initial = emptyList())
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    if (noteToDelete != null) {
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = { Text("Delete Note") },
            text = { Text("Are you sure?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(noteToDelete!!)
                    noteToDelete = null
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(NavRoute.AddNote.createRoute()) }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        LazyVerticalGrid (
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            items(notes.size) { index ->
                val note = notes[index]
                NoteItem(
                    note = note,
                    onDelete = { noteToDelete = note },
                    onEdit = {
                        navController.navigate(NavRoute.AddNote.createRoute(note.id))
                    }
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun NoteItem(note: Note, onDelete: () -> Unit, onEdit: () -> Unit) {
    Card(
        Modifier
            .fillMaxWidth()
            .height(240.dp)
            .combinedClickable(
                onClick = { onEdit() },
                onLongClick = { onDelete() }
            )

    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(note.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5
                )
            }

            val formatter = java.text.SimpleDateFormat("HH:mm\n dd.MM.yyyy")
            val dateText = note.editedTimestamp?.let {
                "Edited: " + formatter.format(java.util.Date(it))
            } ?: run {
                "Created: " + formatter.format(java.util.Date(note.timestamp))
            }
            Text(
                text = dateText,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 5
            )
        }
    }
}