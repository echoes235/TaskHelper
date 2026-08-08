package com.example.taskhelper.ui.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CategoryScreen(viewModel: CategoryScreenViewModel, modifier: Modifier = Modifier) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    if (categories.isEmpty()) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text("还没有使用过分类", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            items(categories, key = { it }) { category ->
                ListItem(
                    headlineContent = { Text(category) },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}
