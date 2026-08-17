package com.example.taskhelper.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskhelper.model.Priority
import com.example.taskhelper.model.Task
import java.text.SimpleDateFormat

@Preview
@Composable
fun ItemTaskPreview() {
    ItemTask(
        task = Task(
            id = 1,
            title = "写周报",
            content = "周五前提交",
            deadline = System.currentTimeMillis(),
            createTime = System.currentTimeMillis(),
            priority = Priority.HIGH,
            isCompleted = true,
            category = "工作",
            tag = listOf("紧急", "团队协作", "待调研", "abc", "ccc", "ddd")
        ),
        onToggle = {},
        onClick = {}
    )
}

@Composable
fun ItemTask(task: Task, onToggle: () -> Unit, onClick: () -> Unit) {
    val priorityColor = when (task.priority) {
        Priority.HIGH -> MaterialTheme.colorScheme.error
        Priority.MID -> Color(0xFFFFA000)
        Priority.LOW -> Color(0xFF4CAF50)
    }
    val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    // 带 Material 主题样式的背景层
    Surface(
        // 占满父容器宽度
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        // 背景色随海拔变深或变浅
        tonalElevation = 1.dp,
        // 四角为圆形
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() }
            )
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${task.title}(${sdf.format(java.util.Date(task.deadline))})",
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    color = if (task.isCompleted)
                        MaterialTheme.colorScheme.onSurface.copy(0.5f) // 透明度改成0.4
                    else MaterialTheme.colorScheme.onSurface,
                )
                if (task.content.isNotBlank()) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = task.content,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1
                    )
                }
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    DeadLineChip(task.deadline)
                    Surface(
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = task.category,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("标签：")
                Spacer(Modifier.size(4.dp))
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    task.tag.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = tag,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
            Box(
                Modifier
                    .width(16.dp)
                    .height(36.dp)
                    .background(priorityColor, RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        Text("还没有任务哦", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
