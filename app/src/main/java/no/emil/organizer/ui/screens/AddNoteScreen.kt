package no.emil.organizer.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.emil.organizer.data.models.Note
import no.emil.organizer.viewmodels.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    viewModel: NoteViewModel,
    navController: NavController,
    noteId: Long? = null,
    onNoteSaved: () -> Unit
) {

    var existingNote by remember { mutableStateOf<Note?>(null) }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(noteId) {
        existingNote = noteId?.let { viewModel.getNoteById(it) }
        existingNote?.let { note ->
            title = note.title
            content = note.content
        }
    }

    val formatter = java.text.SimpleDateFormat("HH:mm dd.MM.yyyy")
    val dateText = existingNote?.editedTimestamp?.let {
        "Edited: " + formatter.format(java.util.Date(it))
    } ?: existingNote?.let {
        "Created: " + formatter.format(java.util.Date(it.timestamp))
    } ?: ""

    if (showDeleteDialog && existingNote != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Note") },
            text = { Text("Are you sure?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(existingNote!!)
                    showDeleteDialog = false
                    navController.popBackStack()
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    val scrollState = rememberScrollState()

    fun saveNote() {
        if (existingNote != null) {
            val updatedNote = existingNote!!.copy(
                title = title,
                content = content,
                editedTimestamp = System.currentTimeMillis()
            )
            viewModel.updateNote(updatedNote)
        } else if (title.isNotBlank() || content.isNotBlank()) {
            val newNote = Note(
                title = title,
                content = content,
                timestamp = System.currentTimeMillis(),
                editedTimestamp = null
            )
            viewModel.addNote(newNote)
        }


    }

    BackHandler {
        saveNote()
        navController.popBackStack()
    }

    Column {
        TopAppBar(
            title = { Text(if (noteId == null) "New Note" else "Edit Note") },
            navigationIcon = {
                IconButton(onClick = {
                    viewModel.saveNoteIfEdited(title, content, existingNote)
                    navController.popBackStack()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier
                    .fillMaxWidth()
            )

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    //.height(200.dp)
                    .weight(1f)
            )

            if (dateText.isNotEmpty()) {
                Text(dateText, style = MaterialTheme.typography.labelSmall)
            }

            existingNote?.let { note ->
                TextButton(onClick = {
                    showDeleteDialog = true
                }) {
                    Text("Delete")
                }
            }

            Button(
                onClick = {
                    /*val note = existingNote
                    if (note != null) {
                        viewModel.updateNote(note.copy(title = title, content = content))
                    } else {
                        viewModel.addNote(Note(title = title, content = content))
                    }
                    onNoteSaved()*/
                    viewModel.saveNoteIfEdited(title, content, existingNote)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }


        }
    }
}