package no.emil.organizer.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.emil.organizer.data.models.TodoItem
import no.emil.organizer.data.models.TodoWithInstance
import no.emil.organizer.data.repositories.TodoRepository
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
class TodoViewModel(
    private val repo: TodoRepository
) : ViewModel() {



    private val startOfToday: Long
        @RequiresApi(Build.VERSION_CODES.O)
        get() = LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()


    @RequiresApi(Build.VERSION_CODES.O)
    val upcomingTodos = repo.getUpcomingTodos(startOfToday)


    init {
        viewModelScope.launch {
            upcomingTodos.collect { todos ->
                todos.forEach {
                    Log.d(
                        "TODO",
                        "DB state ${it.instance.id} completed=${it.instance.completed} ts=${it.instance.completedTimestamp}"
                    )
                }
            }
        }
    }
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

    fun unComplete(todo: TodoWithInstance) {
        viewModelScope.launch {
            repo.markUncompleted(todo.instance)
        }
    }

    fun toggleCompleted(todo: TodoWithInstance) {
        viewModelScope.launch {
            if (todo.instance.completed) {
                repo.markUncompleted(todo.instance)
            } else {
                repo.markCompleted(todo.instance)
            }
        }
    }

    fun adjustDue(todo: TodoWithInstance, deltaMillis: Long) {
        viewModelScope.launch {
            repo.updateDue(
                todo.instance.id,
                todo.instance.dueTimestamp + deltaMillis
            )
        }
    }
}