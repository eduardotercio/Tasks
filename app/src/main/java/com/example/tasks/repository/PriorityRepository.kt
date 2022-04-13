package com.example.tasks.repository

import com.example.tasks.data.local.PriorityDAO
import com.example.tasks.data.model.PriorityModel
import com.example.tasks.data.remote.PrioritySerivceAPI
import com.example.tasks.ui.state.ResourceState
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class PriorityRepository @Inject constructor(
    private val apiPriority: PrioritySerivceAPI,
    private val daoPriority: PriorityDAO
) {

    suspend fun setPriorityList() {
        val response = apiPriority.priorityList()
        if (response.isSuccessful) {
            daoPriority.clear()
            daoPriority.insert(response.body()!!)
        }
    }

    suspend fun list() = daoPriority.list()

}