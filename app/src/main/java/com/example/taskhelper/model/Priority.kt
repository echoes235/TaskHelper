package com.example.taskhelper.model

enum class Priority(val value: Int, val label: String) {
    LOW(0, "低优先级"),
    MID(1, "中优先级"),
    HIGH(2, "高优先级");

    companion object {
        fun fromValue(v: Int) = entries.first { it.value == v }
    }
}
