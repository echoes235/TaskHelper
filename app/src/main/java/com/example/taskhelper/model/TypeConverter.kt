package com.example.taskhelper.model

import androidx.room.TypeConverter

object TypeConverter {
    @TypeConverter
    fun fromList(list: List<String>): String {
        // 用不可见分隔符
        return list.joinToString(separator = "\u0001") { it }
    }

    fun toList(str: String): List<String> {
        return if (str.isEmpty()) emptyList()
        else str.split("\u0001")
    }
}
