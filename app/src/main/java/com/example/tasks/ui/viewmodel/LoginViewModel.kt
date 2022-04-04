package com.example.tasks.ui.viewmodel

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import com.example.tasks.constants.TaskConstants.SHARED
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.model.HeaderModel
import com.example.tasks.repository.UserRepository
import com.example.tasks.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val repository: UserRepository,
) : ViewModel() {

    private val mLogin =
        MutableStateFlow<ResourceState<Response<HeaderModel>>>(ResourceState.Empty())
    val login = mLogin.asStateFlow()

    private val mEmail = MutableStateFlow<String>("")
    val email = mEmail.asStateFlow()

    private val mPassoword = MutableStateFlow<String>("")
    val password = mPassoword.asStateFlow()


    fun updateFields(email: String?, password: String?) {
        mEmail.value = email.toString()
        mPassoword.value = password.toString()
    }

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            try {
                mLogin.value = repository.login(email, password)
            } catch (t: Throwable) {
                Log.e("ErrorLoginVM", t.message.toString())
            }
        }
    }

    /**
     * Verifica se usuário está logado
     */
    fun userIsLogged() = repository.isLogged()

}