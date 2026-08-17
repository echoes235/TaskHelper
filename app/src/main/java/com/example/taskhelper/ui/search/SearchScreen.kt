package com.example.taskhelper.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskhelper.ui.task.ItemTask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewmodel: SearchViewModel) {
    val keyword by viewmodel.keyword.collectAsStateWithLifecycle()
    val tasks by viewmodel.tasks.collectAsStateWithLifecycle()
    Scaffold(
        topBar = { TopAppBar(title = { Text("搜索") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = keyword,
                onValueChange = { viewmodel.search(it) },
                label = { Text("搜索任务") },
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks, key = { it.id }) { value ->
                    ItemTask(
                        value,
                        onToggle = { viewmodel.toggleComplete(value) },
                        onClick = {}
                    )
                }
            }
        }
    }
}
