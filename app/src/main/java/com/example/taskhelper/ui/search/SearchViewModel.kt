package com.example.taskhelper.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhelper.TaskHelperApp
import com.example.taskhelper.model.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel : ViewModel() {
    private val repository = TaskHelperApp.app.taskRepository
    private val _keyword = MutableStateFlow("")

    val keyword: StateFlow<String> = _keyword
    val tasks: StateFlow<List<Task>> = _keyword
        .flatMapLatest { repository.searchTasks(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    val categories: StateFlow<List<String>> = repository.getAllCategory().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun search(keyword: String) {
        _keyword.value = keyword
    }

    fun getTasksByCategory(category: String): Flow<List<Task>> {
        return repository.getTasksByCategory(category)
    }

    fun toggleComplete(task: Task) {
        viewModelScope.launch {
            repository.upsert(task.copy(isCompleted = !task.isCompleted))
        }
    }
}
