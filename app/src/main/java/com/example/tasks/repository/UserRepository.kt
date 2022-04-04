package com.example.tasks.repository

import android.content.SharedPreferences
import com.example.tasks.constants.TaskConstants.SHARED
import com.example.tasks.data.model.HeaderModel
import com.example.tasks.data.remote.UserServiceAPI
import com.example.tasks.ui.state.ResourceState
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiUser: UserServiceAPI,
    private val shared: SharedPreferences
) {

    suspend fun createUser(
        name: String,
        email: String,
        password: String
    ): ResourceState<Response<HeaderModel>> {
        val response = apiUser.createUser(name, email, password)
        return if (response.isSuccessful) {
            save(response)
            ResourceState.Sucess(response)
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

    suspend fun login(email: String, password: String): ResourceState<Response<HeaderModel>> {
        val response = apiUser.login(email, password)
        return if (response.isSuccessful) {
            save(response)
            ResourceState.Sucess(response)
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

    private fun save(response: Response<HeaderModel>) {
        if (!isLogged()) {
            response.body()?.let { header ->
                shared.edit().putString(SHARED.TOKEN_KEY, header.token).apply()
                shared.edit().putString(SHARED.PERSON_KEY, header.personKey).apply()
                shared.edit().putString(SHARED.PERSON_NAME, header.name).apply()
            }
        }
    }

    fun isLogged(): Boolean {
        val token = shared.getString(SHARED.TOKEN_KEY, "") ?: ""
        val key = shared.getString(SHARED.PERSON_KEY, "") ?: ""

        return (token != "" && key != "")
    }

    fun delete() {
        shared.edit().remove(SHARED.TOKEN_KEY).apply()
        shared.edit().remove(SHARED.PERSON_KEY).apply()
        shared.edit().remove(SHARED.PERSON_NAME).apply()
    }
}