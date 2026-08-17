package com.example.taskhelper.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

object Navkey {
    @Serializable
    data object Tasks : NavKey

    @Serializable
    data object Category : NavKey

    @Serializable
    data class TasksByCategory(val category: String) : NavKey

    @Serializable
    data object Search : NavKey

    data class BottomTab(val key: NavKey, val label: String, val icon: ImageVector)

    val bottomTabs = listOf(
        BottomTab(Tasks, "任务", Icons.Filled.Checklist),
        BottomTab(Category, "类别", Icons.Filled.Category),
        BottomTab(Search, "搜索", Icons.Filled.Search)
    )
}
