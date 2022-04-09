package com.example.tasks.data.remote

import com.example.tasks.data.model.PriorityModel
import retrofit2.Response
import retrofit2.http.GET

interface PrioritySerivceAPI {

    @GET("Priority")
    suspend fun priorityList(): Response<List<PriorityModel>>

}