package com.example.tasks.data.remote

import com.example.tasks.data.model.TaskModel
import retrofit2.Response
import retrofit2.http.*

interface TaskServiceAPI {

    @GET("Task")
    suspend fun getAll(): Response<List<TaskModel>>

    @GET("Task/Next7Days")
    suspend fun getNextWeek(): Response<List<TaskModel>>

    @GET("Task/Overdue")
    suspend fun getOverdue(): Response<List<TaskModel>>

    @GET("Task/{id}")
    suspend fun get(@Path(value = "id", encoded = true) id: Int): Response<TaskModel>

    @POST("Task")
    @FormUrlEncoded
    suspend fun insert(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Response<Boolean>

    @HTTP(method = "PUT", path = "Task", hasBody = true)
    @FormUrlEncoded
    suspend fun update(
        @Field("Id") id: Int,
        @Field("PriorityID") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean
    ): Response<Boolean>

    @HTTP(method = "PUT", path = "Task/Complete", hasBody = true)
    @FormUrlEncoded
    suspend fun complete(@Field("Id") id: Int): Response<Boolean>

    @HTTP(method = "PUT", path = "Task/Undo", hasBody = true)
    @FormUrlEncoded
    suspend fun inComplete(@Field("Id") id: Int): Response<Boolean>

    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    suspend fun delete(@Field("Id") id: Int)

    // A anotação "HTTP" deve ser utilizada desta maneira nos métodos PUT e DELETE
    // quando os mesmos utilizarem algum parâmetro na função
}