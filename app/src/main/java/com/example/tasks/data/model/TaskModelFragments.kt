package com.example.tasks.data.model


data class TaskModelFragments(

    val id: Int,

    val priority: String,

    val description: String,

    val dueDate: String,

    var complete: Boolean
)
