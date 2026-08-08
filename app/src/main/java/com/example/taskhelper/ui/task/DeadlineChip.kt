package com.example.taskhelper.ui.task

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Calendar

@Composable
fun DeadlineChip(deadline: Long) {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val days = ((deadline - today) / 86_400_000L).toInt()
    val (label, color) = when {
        days < 0 -> "已过期" to MaterialTheme.colorScheme.error
        days == 0 -> "今天" to MaterialTheme.colorScheme.error
        days == 1 -> "明天" to Color(0xFFFFA000)
        days == 2 -> "后天" to Color(0xFFFFA000)
        else -> "$days 天后" to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Surface(color = color.copy(alpha = .15f), shape = RoundedCornerShape(4.dp)) {
        Text(
            text = label,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
