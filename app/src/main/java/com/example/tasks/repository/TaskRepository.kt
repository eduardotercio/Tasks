package com.example.tasks.repository

import com.example.tasks.data.remote.TaskServiceAPI
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val apiTask: TaskServiceAPI
) {



}