package no.emil.organizer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import no.emil.organizer.data.models.Note
import no.emil.organizer.data.models.TodoInstance
import no.emil.organizer.data.models.TodoItem

@Database(
    entities = [Note::class, TodoItem::class, TodoInstance::class],
    version = 1,
    exportSchema = false
)
abstract class OrganizerDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun todoItemDao(): TodoItemDao
    abstract fun todoInstanceDao(): TodoInstanceDao
}