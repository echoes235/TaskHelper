package com.example.taskhelper.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("SELECT DISTINCT category FROM tasks WHERE category != '' ORDER BY category")
    fun getAllCategory(): Flow<List<String>>

//    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
//    fun getAllByDeadline(): Flow<List<TaskEntity>>
//
//    @Query("SELECT * FROM Tasks ORDER BY createTime ASC")
//    fun getAllByCreateTime(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(task: TaskEntity): Long

    @Delete
    suspend fun delete(task: TaskEntity)
}
