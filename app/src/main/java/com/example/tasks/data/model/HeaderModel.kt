package com.example.tasks.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HeaderModel(

    @SerializedName("token")
    val token: String,

    @SerializedName("personKey")
    val personKey: String,

    @SerializedName("name")
    val name: String

) : Serializable
