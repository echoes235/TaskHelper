package com.example.taskhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskhelper.ui.category.CategoryScreen
import com.example.taskhelper.ui.category.CategoryScreenViewModel
import com.example.taskhelper.ui.task.TaskListScreen
import com.example.taskhelper.ui.task.TaskListViewModel
import com.example.taskhelper.ui.theme.TaskHelperTheme

private enum class Destination(val label: String) {
    TASK("任务"),
    CATEGORY("类别")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TaskHelperTheme {
                var destination by rememberSaveable { mutableStateOf(Destination.TASK) }
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            Destination.entries.forEach { item ->
                                NavigationBarItem(
                                    selected = item == destination,
                                    onClick = { destination = item },
                                    icon = {
                                        Icon(
                                            imageVector = if (item == Destination.TASK) {
                                                Icons.Filled.Checklist
                                            } else {
                                                Icons.Filled.Category
                                            },
                                            contentDescription = item.label
                                        )
                                    },
                                    label = { Text(item.label) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    when (destination) {
                        Destination.TASK -> TaskListScreen(
                            viewModel = viewModel(),
                            modifier = Modifier.padding(padding)
                        )
                        Destination.CATEGORY -> CategoryScreen(
                            viewModel = viewModel<CategoryScreenViewModel>(),
                            modifier = Modifier.padding(padding)
                        )
                    }
                }
            }
        }
    }
}
