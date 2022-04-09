package com.example.tasks.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TaskModel(

    @SerializedName("Id")
    val id: Int,

    @SerializedName("PriorityID")
    val priorityId: Int,

    @SerializedName("Description")
    val description: String,

    @SerializedName("DueDate")
    val dueDate: String,

    @SerializedName("Complete")
    val complete: Boolean

) : Serializable

