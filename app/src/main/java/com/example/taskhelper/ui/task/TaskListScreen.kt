package com.example.taskhelper.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskhelper.TaskHelperApp
import com.example.taskhelper.model.Category
import com.example.taskhelper.model.Priority
import com.example.taskhelper.model.SortBy
import com.example.taskhelper.model.Task
import com.example.taskhelper.reminder.ReminderScheduler
import kotlinx.coroutines.launch

@Preview
@Composable
fun TaskListScreenSample() {
    TaskListScreen(TaskListViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(viewModel: TaskListViewModel = viewModel()) {
    val tasks by viewModel.sortedTasks.collectAsStateWithLifecycle()
    var editingTask by remember { mutableStateOf<Task?>(null) }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val context = TaskHelperApp.app
    var scheduler = remember { ReminderScheduler(context) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("任务") },
                // 顶栏右侧操作按钮区
                actions = {
                    var isMenuExpend by remember { mutableStateOf(false) }
                    IconButton(onClick = { isMenuExpend = true }) {
                        Icon(Icons.Default.MoreHoriz, contentDescription = "排序")
                    }
                    DropdownMenu(
                        expanded = isMenuExpend,
                        onDismissRequest = { isMenuExpend = false }
                    ) {
                        SortBy.entries.forEach { sort ->
                            DropdownMenuItem(
                                text = { Text(sort.label) },
                                onClick = {
                                    viewModel.setSortBy(sort)
                                    isMenuExpend = false
                                }
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                editingTask = Task(
                    id = 0,
                    title = "",
                    content = "",
                    deadline = System.currentTimeMillis() + 24 * 60 * 60 * 1000,
                    createTime = System.currentTimeMillis(),
                    priority = Priority.MID,
                    isCompleted = false,
                    category = Category.OTHER.label,
                    tag = emptyList()
                )
            }) {
                Icon(Icons.Default.Add, contentDescription = "添加任务")
            }
        },
        bottomBar = {}
    ) { padding ->
        if (tasks.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(tasks, key = { it.id }) { task ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        positionalThreshold = { distance -> distance * 0.5f },
                        confirmValueChange = { value ->
                            if (value != SwipeToDismissBoxValue.Settled) {
                                viewModel.delete(task)
                                scope.launch {
                                    val result = snackBarHostState.showSnackbar(
                                        message = "已删除${task.title}",
                                        actionLabel = "撤销",
                                        duration = SnackbarDuration.Short
                                    )
                                    if (result == SnackbarResult.ActionPerformed) {
                                        viewModel.upsert(task)
                                    }
                                }
                                true
                            } else true
                        }
                    )
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.errorContainer)
                                    .padding(horizontal = 16.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "删除",
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        },
                    ) {
                        ItemTask(
                            task = task,
                            onToggle = { viewModel.toggleComplete(task) },
                            onClick = {
                                editingTask = task
                            }
                        )
                    }
                }
            }
        }
    }
    editingTask?.let { task ->
        EditTaskDialog(
            initTask = task,
            onDismiss = { editingTask = null },
            onConfirm = { newTask ->
                viewModel.upsert(newTask)
                scheduler.schedule(newTask)
                editingTask = null
            }
        )
    }
}
