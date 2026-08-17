package com.example.taskhelper.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.taskhelper.model.Category
import com.example.taskhelper.model.Priority
import com.example.taskhelper.model.Tag
import com.example.taskhelper.model.Task
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TagSelector(
    tags: List<String>,
    _selected: Set<String> = emptySet(),
    columns: Int = 3,
    onSave: (Set<String>) -> Unit
) {
    var expended by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(_selected) }
    OutlinedButton(
        onClick = { expended = !expended },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("选择标签")
    }
    if (expended) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            // 通过改变背景色调来表达「这个面比底层高一点」的视觉层级
            tonalElevation = 1.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tags) { tag ->
                        TagItem(
                            tag,
                            selected = tag in selected,
                            onClick = {
                                selected = if (tag in selected) selected - tag else selected + tag
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { expended = false }) {
                        Text("取消")
                    }
                    Spacer(Modifier.size(8.dp))
                    TextButton(onClick = {
                        onSave(selected)
                        expended = false
                    }) {
                        Text("确定")
                    }
                }
            }
        }
    }
}

@Composable
private fun TagItem(tag: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = if (selected) Icons.Default.CheckCircle else Icons.Default.Circle,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(tag, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskDialog(initTask: Task, onDismiss: () -> Unit, onConfirm: (Task) -> Unit) {
    var title by remember { mutableStateOf(initTask.title) }
    var content by remember { mutableStateOf(initTask.content) }
    var deadline by remember { mutableStateOf(initTask.deadline) }
    var priority by remember { mutableStateOf(initTask.priority) }
    var category by remember { mutableStateOf(initTask.category) }
    var tag by remember { mutableStateOf(initTask.tag) }
    var showDatepicker by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    var isCreate = initTask.id == 0L
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isCreate) "新建任务" else "编辑任务")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("标题") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("备注") },
                    minLines = 2
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Priority.entries.forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p.label) }
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = { showDatepicker = true }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("设置截止日期")
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "当前日期：${sdf.format(Date(deadline))}",
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ExposedDropdownMenuBox(
                        modifier = Modifier.fillMaxWidth(),
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("分类") },
                            // 自动旋转的下拉箭头
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            // 将这个Outline标记为Exposed的锚点
                            modifier = Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            Category.entries.forEach { it ->
                                DropdownMenuItem(
                                    text = { Text(it.label) },
                                    onClick = {
                                        category = it.label
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                TagSelector(Tag.entries.map { it.label }) { strings ->
                    tag = strings.toList()
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(
                            initTask.copy(
                                title = title,
                                content = content,
                                deadline = deadline,
                                priority = priority,
                                category = category,
                                tag = tag
                            )
                        )
                    }
                }
            ) {
                Text(if (isCreate) "保存" else "确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        }
    )
    if (showDatepicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = deadline)
        DatePickerDialog(
            onDismissRequest = { showDatepicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let { deadline = it }
                        showDatepicker = false
                    }
                ) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showDatepicker = false }) { Text("取消") }
            }
        ) { DatePicker(state = state) }
    }
}
