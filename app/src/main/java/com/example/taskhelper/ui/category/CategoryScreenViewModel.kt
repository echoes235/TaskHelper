package com.example.taskhelper.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskhelper.TaskHelperApp
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CategoryScreenViewModel : ViewModel() {
    val categories: StateFlow<List<String>> = TaskHelperApp.app.taskRepository.getAllCategory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
