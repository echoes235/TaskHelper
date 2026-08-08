package com.example.taskhelper.model

data class Task(
    val id: Long,
    val title: String,
    val content: String,
    val deadline: Long,
    val createTime: Long,
    val priority: Priority,
    val isCompleted: Boolean,
    val category: String,
    val tag: List<String>
)

