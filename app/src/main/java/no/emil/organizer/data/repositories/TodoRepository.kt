package no.emil.organizer.data.repositories

import kotlinx.coroutines.flow.Flow
import no.emil.organizer.data.database.TodoInstanceDao
import no.emil.organizer.data.database.TodoItemDao
import no.emil.organizer.data.models.TodoInstance
import no.emil.organizer.data.models.TodoItem
import no.emil.organizer.data.models.TodoWithInstance

class TodoRepository(
    private val itemDao: TodoItemDao,
    private val instanceDao: TodoInstanceDao
) {
    fun getUpcomingTodos(startOfToday: Long): Flow<List<TodoWithInstance>> = instanceDao.getUpcomingWithItem(startOfToday)

    suspend fun createTodo(
        item: TodoItem,
        dueTimestamp: Long?
    ) {
        val itemId = itemDao.insert(item)

        dueTimestamp?.let {
            instanceDao.insert(
                TodoInstance(
                    todoItemId = itemId,
                    dueTimestamp = it
                )
            )
        }
    }

    suspend fun markCompleted(instance: TodoInstance) {
        instanceDao.markCompleted(instance.id)
    }

    suspend fun markUncompleted(instance: TodoInstance) {
        instanceDao.markUncompleted(instance.id)
    }

    suspend fun updateDue(id: Long, newDue: Long) {
        instanceDao.updateDueTimestamp(id, newDue);
    }
}