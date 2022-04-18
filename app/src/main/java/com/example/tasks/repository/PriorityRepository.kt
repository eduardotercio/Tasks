package com.example.tasks.repository

import com.example.tasks.data.local.PriorityDAO
import com.example.tasks.data.remote.PrioritySerivceAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PriorityRepository @Inject constructor(
    private val apiPriority: PrioritySerivceAPI,
    private val daoPriority: PriorityDAO
) {

    suspend fun setPriorityList() = withContext(Dispatchers.IO) {
        val response = apiPriority.priorityList()
        if (response.isSuccessful) {
            daoPriority.clear()
            daoPriority.insert(response.body()!!)
        }
    }

    suspend fun list() = daoPriority.list()

    suspend fun priority(id: Int) = daoPriority.priority(id)

}