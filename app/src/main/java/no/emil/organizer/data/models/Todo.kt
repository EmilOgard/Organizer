package no.emil.organizer.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String = "",
    val createdTimestamp: Long = System.currentTimeMillis(),
    val defaultDueTimestamp: Long? = null,
    val repeatPattern: String? = null,
    val isActive: Boolean = true
)

@Entity(
    tableName = "todo_instances",
    foreignKeys = [
        ForeignKey(
            entity = TodoItem::class,
            parentColumns = ["id"],
            childColumns = ["todoItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TodoInstance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val todoItemId: Long,
    val dueTimestamp: Long,
    val completed: Boolean = false,
    val completedTimestamp: Long? = null
)

data class TodoWithInstance(
    @Embedded val instance: TodoInstance,
    @Relation(
        parentColumn = "todoItemId",
        entityColumn = "id"
    )
    val item: TodoItem
)