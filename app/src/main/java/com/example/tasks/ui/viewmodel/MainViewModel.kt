package com.example.tasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tasks.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    fun logout() = repository.delete()

}