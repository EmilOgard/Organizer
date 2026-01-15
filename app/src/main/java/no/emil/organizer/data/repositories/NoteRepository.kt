package no.emil.organizer.data.repositories

import kotlinx.coroutines.flow.Flow
import no.emil.organizer.data.database.NoteDao
import no.emil.organizer.data.models.Note

class NoteRepository(private val dao: NoteDao) {

    val notes: Flow<List<Note>> = dao.getAllNotes()

    suspend fun addNote(note: Note) {
        dao.insert(note)
    }

    suspend fun deleteNote(note: Note) {
        dao.delete(note)
    }

    suspend fun updateNote(note: Note) {
        dao.update(note)
    }

    suspend fun getNoteById(id: Long): Note? {
        return dao.getById(id)
    }
}