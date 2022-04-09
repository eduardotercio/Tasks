package com.example.tasks.repository

import android.content.SharedPreferences
import com.example.tasks.constants.TaskConstants.SHARED
import com.example.tasks.data.model.HeaderModel
import com.example.tasks.data.remote.AuthServiceAPI
import com.example.tasks.ui.state.ResourceState
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiAuth: AuthServiceAPI,
    private val shared: SharedPreferences
) {

    suspend fun createUser(
        name: String,
        email: String,
        password: String
    ): ResourceState<Response<HeaderModel>> {
        val response = apiAuth.createUser(name, email, password)
        return if (response.isSuccessful) {
            save(response)
            ResourceState.Sucess(response)
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

    suspend fun login(email: String, password: String): ResourceState<Response<HeaderModel>> {
        val response = apiAuth.login(email, password)
        return if (response.isSuccessful) {
            save(response)
            ResourceState.Sucess(response)
        } else {
            val message = Gson().fromJson(response.errorBody()!!.charStream(), String::class.java)
            ResourceState.Error(message)
        }
    }

    // Verifica se o usuário está logado
    fun isLogged(): Boolean {
        val token = shared.getString(SHARED.TOKEN_KEY, "") ?: ""
        val key = shared.getString(SHARED.PERSON_KEY, "") ?: ""

        return (token != "" && key != "")
    }

    // Se Não estiver logado, salva os dados retornados da API no shared,
    // para usar como Header posteriormente
    private fun save(response: Response<HeaderModel>) {
        if (!isLogged()) {
            response.body()?.let { header ->
                shared.edit().putString(SHARED.TOKEN_KEY, header.token).apply()
                shared.edit().putString(SHARED.PERSON_KEY, header.personKey).apply()
                shared.edit().putString(SHARED.PERSON_NAME, header.name).apply()
            }
        }
    }

    // Chamada para deletar os dados do Header, assim deslogando o usuário
    fun delete() {
        shared.edit().remove(SHARED.TOKEN_KEY).apply()
        shared.edit().remove(SHARED.PERSON_KEY).apply()
        shared.edit().remove(SHARED.PERSON_NAME).apply()
    }
}