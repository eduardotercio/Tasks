package com.example.tasks.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.tasks.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllTasksViewModel @Inject constructor(
    application: Application,
    //private val repository: UserRepository
) : ViewModel() {


}