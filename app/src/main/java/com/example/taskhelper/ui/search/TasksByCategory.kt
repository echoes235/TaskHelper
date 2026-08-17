package com.example.taskhelper.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskhelper.ui.task.ItemTask
import com.example.taskhelper.ui.task.TaskListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksByCategory(
    viewmodel: SearchViewModel,
    taskViewModel: TaskListViewModel,
    category: String
) {
    val tasks by viewmodel.getTasksByCategory(category)
        .collectAsStateWithLifecycle(initialValue = emptyList())
    Scaffold(
        topBar = { TopAppBar(title = { Text(category) }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks, key = { it.id }) { value ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    ItemTask(
                        value,
                        onToggle = { taskViewModel.toggleComplete(value) },
                        onClick = {}
                    )
                }
            }
        }
    }
}
