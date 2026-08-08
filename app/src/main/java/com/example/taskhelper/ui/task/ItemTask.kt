package com.example.taskhelper.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.taskhelper.model.Priority
import com.example.taskhelper.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ItemTask(task: Task, onToggle: () -> Unit, onClick: () -> Unit) {
    val priorityColor = when (task.priority) {
        Priority.HIGH -> MaterialTheme.colorScheme.error
        Priority.MID -> Color(0xFFFFA000)
        Priority.LOW -> Color(0xFF4CAF50)
    }
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        tonalElevation = 1.dp,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = task.isCompleted, onCheckedChange = { onToggle() })
            Spacer(Modifier.width(8.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "${task.title}（${formatter.format(Date(task.deadline))}）",
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = .5f)
                    else MaterialTheme.colorScheme.onSurface
                )
                if (task.content.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(task.content, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                }
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DeadlineChip(task.deadline)
                    if (task.category.isNotBlank()) LabelChip(task.category)
                }
                if (task.tag.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) { task.tag.forEach { tag -> LabelChip(tag) } }
                }
            }
            Spacer(Modifier.width(8.dp))
            Box(Modifier.width(16.dp).height(36.dp).background(priorityColor, RoundedCornerShape(2.dp)))
        }
    }
}

@Composable
private fun LabelChip(text: String) {
    Surface(color = MaterialTheme.colorScheme.error.copy(alpha = .15f), shape = RoundedCornerShape(4.dp)) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 96.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("还没有任务哦", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
