package com.example.tasks.repository

import com.example.tasks.data.model.TaskModel
import com.example.tasks.data.remote.TaskServiceAPI
import com.example.tasks.ui.state.ResourceState
import com.google.gson.Gson
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val apiTask: TaskServiceAPI
) {

    suspend fun save(task: TaskModel) : ResourceState<Response<Boolean>> {
        val response = apiTask.insert(task.priorityId,task.description,task.dueDate,task.complete)
        return if (response.isSuccessful){
            ResourceState.Sucess(response)
        } else{
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

}