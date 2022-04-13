package com.example.tasks.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.model.HeaderModel
import com.example.tasks.data.model.PriorityModel
import com.example.tasks.repository.AuthRepository
import com.example.tasks.repository.PriorityRepository
import com.example.tasks.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val authRepository: AuthRepository,
    private val priorityRepository: PriorityRepository
) : ViewModel() {

    private val mLogin =
        MutableStateFlow<ResourceState<Response<HeaderModel>>>(ResourceState.Empty())
    val login = mLogin.asStateFlow()

    private val mPriorityList =
        MutableStateFlow<ResourceState<Response<List<PriorityModel>>>>(ResourceState.Empty())
    val priorityList = mPriorityList.asStateFlow()

//    private val mEmail = MutableStateFlow<String>("")
//    val email = mEmail.asStateFlow()
//
//    private val mPassoword = MutableStateFlow<String>("")
//    val password = mPassoword.asStateFlow()

    private val mToast = MutableSharedFlow<String>()
    val toast = mToast.asSharedFlow()

//    fun updateFields(email: String?, password: String?) {
//        mEmail.value = email.toString()
//        mPassoword.value = password.toString()
//    }

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        viewModelScope.launch {
            try {
                priorityRepository.setPriorityList()
                mLogin.value = authRepository.login(email, password)
            } catch (t: Throwable) {
                if (email == "" || password == "") {
                    mToast.emit(mLogin.value.message ?: "Preencha todos os campos.")
                } else {
                    mToast.emit(mLogin.value.message ?: "Falha na conexão.")
                }

                Log.e("ErrorLoginVM", t.message.toString())
            } finally {
                if (email != "" && password != "") {
                    mToast.emit(mLogin.value.message ?: "Falha na conexão.")
                }
            }
        }
    }

    /**
     * Verifica se usuário está logado
     */
    fun userIsLogged() = authRepository.isLogged()

}