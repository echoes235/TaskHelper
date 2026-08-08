package com.example.taskhelper.model.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    indices = [Index("deadline"), Index("priority"), Index("createTime")]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val deadline: Long,
    val createTime: Long,
    val priority: Int,
    val isCompleted: Boolean,
    val category: String,
    val tag: String
)
