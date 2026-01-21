package no.emil.organizer.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import no.emil.organizer.data.models.TodoWithInstance
import no.emil.organizer.viewmodels.TodoViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun formatDue(dueMillis: Long, now: Long): String {
    val diff = dueMillis - now

    if (diff <= 0) return "Overdue"

    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    //val days = TimeUnit.MILLISECONDS.toDays(diff)

    val today = LocalDate.ofInstant(Instant.ofEpochMilli(now), ZoneId.systemDefault())
    val dueDate = LocalDate.ofInstant(Instant.ofEpochMilli(dueMillis), ZoneId.systemDefault())

    return when {
        minutes < 60 -> "$minutes min"
        hours < 24 && today == dueDate -> "$hours hrs"
        dueDate == today.plusDays(1) -> "Tomorrow"
        else -> {
            val sdf = SimpleDateFormat("dd MMM", Locale.getDefault())
            sdf.format(dueMillis)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun DueText(
    dueMillis: Long,
    isCompleted: Boolean,
    onToggleCompleted: () -> Unit
) {
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(dueMillis) {
        while (true) {
            now = System.currentTimeMillis()
            delay(60_000L)
        }
    }

    Text(
        text = if (isCompleted) "Completed"
                else formatDue(dueMillis, now),
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.clickable {
            onToggleCompleted()
        }
    )
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {

    val todos by viewModel.upcomingTodos.collectAsState(initial = emptyList())

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val calendar = remember { Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }}

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var selectedYear by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    var selectedHour by remember { mutableIntStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableIntStateOf(calendar.get(Calendar.MINUTE)) }


    fun calculateDueTimestamp(): Long {
        val cal = Calendar.getInstance()
        cal.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0)
        return cal.timeInMillis

    }


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

        Text(
            "Due: $selectedDay/${selectedMonth+1}/$selectedYear " +
                    "$selectedHour:${selectedMinute.toString().padStart(2, '0')}"
        )
        Spacer(Modifier.height(4.dp))

        Row {
            Button(
                onClick = { showDatePicker = true },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Pick date")
            }
            Spacer(Modifier.height(4.dp))
            Button(
                onClick = { showTimePicker = true },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Pick time")
            }
        }

        Spacer(Modifier.height(4.dp))


        Button(
            onClick = {
                if (title.isNotBlank()) {
                    viewModel.addTodo(
                        title = title,
                        description = description,
                        dueTimestamp = calculateDueTimestamp()
                    )
                    title = ""
                    description = ""
                }
            },
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
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
                            Text(
                                todo.item.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Spacer(Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            DueText(
                                todo.instance.dueTimestamp,
                                isCompleted = todo.instance.completed,
                                onToggleCompleted = {
                                    viewModel.toggleCompleted(todo)
                                }
                            )

                            Spacer(Modifier.weight(1f))

                            AdjustButtons(viewModel, todo)

                        }

                    }
                }
            }
        }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val cal = Calendar.getInstance().apply { timeInMillis = millis }
                                selectedYear = cal.get(Calendar.YEAR)
                                selectedMonth = cal.get(Calendar.MONTH)
                                selectedDay = cal.get(Calendar.DAY_OF_MONTH)
                            }
                            showDatePicker = false
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) { Text("OK") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePicker) {
            val timeState = rememberTimePickerState(
                initialHour = selectedHour,
                initialMinute = selectedMinute
            )

            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        selectedHour = timeState.hour
                        selectedMinute = timeState.minute
                        showTimePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showTimePicker = false },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                    ) { Text("Cancel") }
                },
                text = { TimePicker(state = timeState)}
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AdjustButtons(
    viewModel: TodoViewModel,
    todo: TodoWithInstance
) {
    Row {
        AdjustButton("+1h"){
            viewModel.adjustDue(todo, TimeUnit.HOURS.toMillis(1))
        }

        AdjustButton("-1h"){
            viewModel.adjustDue(todo, -TimeUnit.HOURS.toMillis(1))
        }

        AdjustButton("+1d") {
            viewModel.adjustDue(todo, TimeUnit.DAYS.toMillis(1))
        }

        AdjustButton("-1d"){
            viewModel.adjustDue(todo, -TimeUnit.DAYS.toMillis(1))
        }
    }
}

@Composable
private fun AdjustButton(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable { onClick() },
        color = MaterialTheme.colorScheme.primary
    )
}