package com.example.taskhelper.model

/** Stores a list in Room without losing spaces or punctuation within individual tags. */
object TypeConverter {
    private const val Separator = "\u0001"

    fun fromList(list: List<String>): String = list.joinToString(Separator)

    fun toList(value: String): List<String> =
        if (value.isEmpty()) emptyList() else value.split(Separator)
}
