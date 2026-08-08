package com.example.taskhelper.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhelper.TaskHelperApp
import com.example.taskhelper.model.SortBy
import com.example.taskhelper.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface TaskState {
    object Loading : TaskState
    data class Success(val tasks: List<Task>) : TaskState
    data class Error(val msg: String) : TaskState
}

class TaskListViewModel : ViewModel() {
    private val _sortBy = MutableStateFlow(SortBy.PRIORITY)
    val sortBy: StateFlow<SortBy> = _sortBy.asStateFlow()
    private val repository = TaskHelperApp.app.taskRepository
    val sortedTasks: StateFlow<List<Task>> =
        combine(repository.getAllTasks(), sortBy) { tasks, sortBy ->
            sortTasks(tasks, sortBy)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private fun sortTasks(tasks: List<Task>, sortBy: SortBy): List<Task> {
        val (active, completed) = tasks.partition { !it.isCompleted }
        val sortedActive = when (sortBy) {
            SortBy.PRIORITY -> active.sortedByDescending { it.priority.ordinal }
            SortBy.DEADLINE -> active.sortedBy { it.deadline }
            SortBy.CREATETIME -> active.sortedBy { it.createTime }
        }
        val sortedCompleted = completed.sortedByDescending { it.deadline }
        return sortedActive + sortedCompleted
    }

    fun upsert(task: Task) {
        viewModelScope.launch {
            repository.upsert(task)
        }
    }

    fun delete(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    fun setSortBy(sortBy: SortBy) {
        _sortBy.value = sortBy
    }

    fun toggleComplete(task: Task) {
        upsert(task.copy(isCompleted = !task.isCompleted))
    }
}
