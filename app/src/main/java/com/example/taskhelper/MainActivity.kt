package com.example.taskhelper

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.taskhelper.navigation.Navkey
import com.example.taskhelper.ui.category.CategoryScreen
import com.example.taskhelper.ui.search.SearchScreen
import com.example.taskhelper.ui.search.SearchViewModel
import com.example.taskhelper.ui.search.TasksByCategory
import com.example.taskhelper.ui.task.TaskListScreen
import com.example.taskhelper.ui.task.TaskListViewModel
import com.example.taskhelper.ui.theme.TaskHelperTheme

const val NOTIFICATION_CHANNEL_ID = "task_reminder"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Android 13+时需要请求发送通知的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        enableEdgeToEdge()
        setContent {
            TaskHelperTheme {
                val navBackStack = rememberNavBackStack(Navkey.Tasks)
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            Navkey.bottomTabs.forEach { item ->
                                NavigationBarItem(
                                    selected = item.key == navBackStack.lastOrNull(),
                                    onClick = {
                                        navBackStack.clear()
                                        navBackStack.add(item.key)
                                    },
                                    icon = { Icon(item.icon, item.label) },
                                    label = { Text(item.label) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    NavDisplay(
                        modifier = Modifier.padding(padding),
                        backStack = navBackStack,
                        onBack = { navBackStack.removeLastOrNull() },
                        entryProvider = entryProvider {
                            entry<Navkey.Tasks> {
                                val viewModel: TaskListViewModel = viewModel()
                                TaskListScreen(viewModel)
                            }
                            entry<Navkey.Category> {
                                val viewmodel: SearchViewModel = viewModel()
                                CategoryScreen(viewmodel, onClick = { category ->
                                    navBackStack.add(Navkey.TasksByCategory(category))
                                })
                            }
                            entry<Navkey.TasksByCategory> {
                                val viewModel: SearchViewModel = viewModel()
                                val taskViewModel: TaskListViewModel = viewModel()
                                val category = it.category
                                TasksByCategory(viewModel, taskViewModel, category)
                            }
                            entry<Navkey.Search> {
                                val viewmodel: SearchViewModel = viewModel()
                                SearchScreen(viewmodel)
                            }
                        }
                    )
                }
            }
        }
        createNotificationChannel()
    }

    // 从Android 8.0开始强制规定每个通知必须挂在一个具体的Channel上，用户可以自定义重要性、行为等。
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "任务提醒",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "任务到期提醒"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
