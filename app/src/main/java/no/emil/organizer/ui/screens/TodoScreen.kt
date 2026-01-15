package no.emil.organizer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import no.emil.organizer.viewmodels.TodoViewModel

@Composable
fun TodoScreen(viewModel: TodoViewModel) {

    val todos by viewModel.upcomingTodos.collectAsState(initial = emptyList())

    Column {
        todos.forEach { todo ->
            Text(todo.item.title)
            Button(onClick = { viewModel.complete(todo) }) {
                Text("Complete")
            }
        }
        Button(onClick = {
            viewModel.addTodo(title = "New Task", description = "", dueTimestamp = System.currentTimeMillis() + 36500_000)
        }) {
            Text("Add Task")
        }
    }
}