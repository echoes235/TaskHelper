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
fun DeadLineChip(deadline: Long) {
    val now = System.currentTimeMillis()
    val today = Calendar.getInstance().apply {
        timeInMillis = now
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.HOUR, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val diffDay = ((deadline - today) / (24 * 60 * 60 * 1000)).toInt()
    val (text, color) = when {
        diffDay < 0 -> "已过期" to MaterialTheme.colorScheme.error
        diffDay == 0 -> "今天" to MaterialTheme.colorScheme.error
        diffDay == 1 -> "明天" to Color(0xFFFFA000)
        diffDay == 2 -> "后天" to Color(0xFFFFA000)
        else -> "$diffDay 天后" to MaterialTheme.colorScheme.surfaceVariant
    }
    Surface(
        color = color.copy(alpha = 0.15f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
