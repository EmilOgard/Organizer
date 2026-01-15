package no.emil.organizer.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.emil.organizer.data.models.TodoItem
import no.emil.organizer.data.models.TodoWithInstance
import no.emil.organizer.data.repositories.TodoRepository

class TodoViewModel(
    private val repo: TodoRepository
) : ViewModel() {
    val upcomingTodos = repo.getUpcomingTodos()

    fun addTodo(
        title: String,
        description: String,
        dueTimestamp: Long?
    ) {
        viewModelScope.launch {
            repo.createTodo(
                TodoItem(
                    title = title,
                    description = description
                ),
                dueTimestamp
            )
        }
    }

    fun complete(todo: TodoWithInstance) {
        viewModelScope.launch {
            repo.markCompleted(todo.instance)
        }
    }
}