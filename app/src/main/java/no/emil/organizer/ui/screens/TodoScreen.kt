package no.emil.organizer.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.emil.organizer.viewmodels.TodoViewModel

@Composable
fun TodoScreen(viewModel: TodoViewModel) {

    val todos by viewModel.upcomingTodos.collectAsState(initial = emptyList())

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val defaultDue = System.currentTimeMillis() + 7_200_000
    var dueTimeStamp by remember { mutableLongStateOf(defaultDue) }

    Column(Modifier.padding(16.dp)) {


        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Task description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    viewModel.addTodo(
                        title = title,
                        description = description,
                        dueTimestamp = dueTimeStamp
                    )
                    title = ""
                    description = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add task")
        }

        Spacer(Modifier.height(20.dp))


        LazyColumn {
            items(todos) { todo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(todo.item.title, style = MaterialTheme.typography.titleMedium)
                        if (todo.item.description.isNotBlank()) {
                            Text(todo.item.description, style = MaterialTheme.typography.bodyMedium)
                        }

                        Spacer(Modifier.height(4.dp))

                        Button(
                            onClick = { viewModel.complete(todo) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Complete")
                        }
                    }
                }
            }
        }
    }
}