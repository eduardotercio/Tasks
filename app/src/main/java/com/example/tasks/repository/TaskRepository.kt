package com.example.tasks.repository

import com.example.tasks.data.model.TaskModelResponse
import com.example.tasks.data.remote.TaskServiceAPI
import com.example.tasks.ui.state.ResourceState
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val apiTask: TaskServiceAPI
) {

    /** SAVE*/
    suspend fun save(task: TaskModelResponse): ResourceState<Response<Boolean>> {
        val response =
            apiTask.insert(task.priorityId, task.description, task.dueDate, task.complete)
        return if (response.isSuccessful) {
            ResourceState.Sucess(response)
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

    /** UPDATE*/
    suspend fun update(task: TaskModelResponse): ResourceState<Boolean> {
        val response =
            apiTask.update(task.id, task.priorityId, task.description, task.dueDate, task.complete)

        return if (response.isSuccessful) {
            ResourceState.Sucess(response.body())
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

    /** GET ONE*/
    suspend fun task(taskId: Int): ResourceState<TaskModelResponse> {
        val response = apiTask.get(taskId)
        return if (response.isSuccessful) {
            ResourceState.Sucess(response.body())
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

    /** GET LIST*/
    suspend fun list(): ResourceState<List<TaskModelResponse>> {
        val response = apiTask.getAll()
        return handleResponseList(response)
    }

    /** GET LIST*/
    suspend fun listNextWeek(): ResourceState<List<TaskModelResponse>> {
        val response = apiTask.getNextWeek()
        return handleResponseList(response)
    }

    /** GET LIST*/
    suspend fun listOverdue(): ResourceState<List<TaskModelResponse>> {
        val response = apiTask.getOverdue()
        return handleResponseList(response)
    }

    private fun handleResponseList(response: Response<List<TaskModelResponse>>): ResourceState<List<TaskModelResponse>> {
        return if (response.isSuccessful) {
            ResourceState.Sucess(response.body())
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }

    }

    suspend fun updateStatus(id: Int, complete: Boolean): ResourceState<Boolean> {
        return if (complete) {
            val response = apiTask.undo(id)
            handleResponseStatus(response)
        } else {
            val response = apiTask.complete( id)
            handleResponseStatus(response)
        }
    }

    private fun handleResponseStatus(response: Response<Boolean>): ResourceState<Boolean> {
        return if (response.isSuccessful) {
            ResourceState.Sucess(response.body())
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

    suspend fun delete(id: Int): ResourceState<Boolean> {
        val response = apiTask.delete(id)
        return if (response.isSuccessful) {
            ResourceState.Sucess(response.body())
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

}