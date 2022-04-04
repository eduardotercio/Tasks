package com.example.tasks.data.remote

import com.example.tasks.data.model.HeaderModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserServiceAPI {

    @POST("Authentication/Login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<HeaderModel>

    @POST("Authentication/Create")
    @FormUrlEncoded
    suspend fun createUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("receiveNews") news: Boolean = true
    ): Response<HeaderModel>
}