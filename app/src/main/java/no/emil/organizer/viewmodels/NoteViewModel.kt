package no.emil.organizer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.emil.organizer.data.models.Note
import no.emil.organizer.data.repositories.NoteRepository

class NoteViewModel(private val repo: NoteRepository) : ViewModel() {

    private val rawNotes = repo.notes

    val notes = rawNotes.map { notes ->
        notes.sortedByDescending { note ->
            note.editedTimestamp ?: note.timestamp
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    fun addNote(note: Note) {
        viewModelScope.launch {
            repo.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch { repo.updateNote(note) }
    }

    suspend fun getNoteById(id: Long): Note? {
        return repo.getNoteById(id)
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            repo.deleteNote(note)
        }
    }

    fun saveNoteIfEdited(title: String, content: String, existingNote: Note?) {
        if (existingNote != null) {

            val changed = title != existingNote.title || content != existingNote.content

            val updated = existingNote.copy(
                title = title,
                content = content,
                editedTimestamp = if (changed)
                    System.currentTimeMillis()
                else
                    existingNote.editedTimestamp
            )
            updateNote(updated)
        } else if (title.isNotBlank() || content.isNotBlank()) {
            addNote(Note(title = title, content = content, timestamp = System.currentTimeMillis(), editedTimestamp = null))
        }
    }

}