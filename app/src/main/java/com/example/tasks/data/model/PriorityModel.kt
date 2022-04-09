package com.example.tasks.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "priority")
data class PriorityModel(

    @SerializedName("Id")
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,

    @SerializedName("Description")
    @ColumnInfo(name = "description")
    val description: String

) : Serializable
