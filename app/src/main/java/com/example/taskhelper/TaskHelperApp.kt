package com.example.taskhelper

import android.app.Application
import com.example.taskhelper.model.TaskRepository
import com.example.taskhelper.model.local.TaskDatabase

class TaskHelperApp : Application() {
    val database by lazy { TaskDatabase.get(this) }
    val taskRepository by lazy { TaskRepository(database.taskDao()) }

    companion object {
        lateinit var app: TaskHelperApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}
