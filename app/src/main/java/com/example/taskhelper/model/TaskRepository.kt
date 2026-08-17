package com.example.taskhelper.model

import com.example.taskhelper.model.local.TaskDao
import com.example.taskhelper.model.local.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

private fun TaskEntity.toDomain() = Task(
    id = id,
    title = title,
    content = content,
    deadline = deadline,
    createTime = createTime,
    priority = Priority.fromValue(priority),
    isCompleted = isCompleted,
    category = category,
    tag = TypeConverter.toList(tag)
)

private fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    content = content,
    deadline = deadline,
    createTime = createTime,
    priority = priority.value,
    isCompleted = isCompleted,
    category = category,
    tag = TypeConverter.fromList(tag)
)

class TaskRepository(private val dao: TaskDao) {
    fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks().map { list -> list.map { it.toDomain() } }
    }

    fun getAllCategory(): Flow<List<String>> {
        return dao.getAllCategory()
    }

    fun getTasksByCategory(category: String): Flow<List<Task>> {
        return dao.getTasksByCategory(category).map { list -> list.map { it.toDomain() } }
    }

    fun searchTasks(keyword: String): Flow<List<Task>> {
        if (keyword.isBlank()) return flowOf(emptyList())
        else return dao.searchTasks(keyword).map { list -> list.map { it.toDomain() } }
    }

//    fun getAllByDeadline(): Flow<List<Task>> {
//        return dao.getAllByDeadline().map { list -> list.map { it.toDomain() } };
//    }
//
//    fun getAllByCreateTime(): Flow<List<Task>> {
//        return dao.getAllByCreateTime().map { list -> list.map { it.toDomain() } }
//    }

    suspend fun upsert(task: Task): Long {
        return dao.upsert(task.toEntity())
    }

    suspend fun delete(task: Task) {
        return dao.delete(task.toEntity())
    }
}
