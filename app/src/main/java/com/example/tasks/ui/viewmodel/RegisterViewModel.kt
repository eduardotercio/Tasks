package com.example.tasks.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.model.HeaderModel
import com.example.tasks.repository.AuthRepository
import com.example.tasks.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    application: Application,
    private val repository: AuthRepository,
) : ViewModel() {

    private val mCreate =
        MutableStateFlow<ResourceState<Response<HeaderModel>>>(ResourceState.Empty())
    val create = mCreate.asStateFlow()


    fun create(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                mCreate.value = repository.createUser(name, email, password)
            } catch (t: Throwable) {
                Log.e("ErrorRegisterVM", t.message.toString())
                when (t) {
                    is IOException -> {
                        mCreate.value = ResourceState.Error("Erro na conexão com a Internet")
                    }
                    else -> {
                        mCreate.value = ResourceState.Error("Falha na conversão de dados")
                    }
                }
            }
        }
    }

}