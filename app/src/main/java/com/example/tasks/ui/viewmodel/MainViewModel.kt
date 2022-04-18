package com.example.tasks.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.tasks.repository.AuthRepository
import com.example.tasks.util.constants.TaskConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val shared: SharedPreferences
) : ViewModel() {

    private val mName = MutableStateFlow<String>("")
    val name = mName.asStateFlow()


    fun name() {
        mName.value = shared.getString(TaskConstants.SHARED.PERSON_NAME, "Name") ?: "Name"
    }

    fun logout() = repository.delete()

}