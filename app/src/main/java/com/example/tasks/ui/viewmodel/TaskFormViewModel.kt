package com.example.tasks.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.model.PriorityModel
import com.example.tasks.data.model.TaskModelResponse
import com.example.tasks.repository.PriorityRepository
import com.example.tasks.repository.TaskRepository
import com.example.tasks.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TaskFormViewModel @Inject constructor(
    application: Application,
    private val priorityRepository: PriorityRepository,
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val mSave = MutableStateFlow<ResourceState<Response<Boolean>>>(ResourceState.Empty())
    val save = mSave.asStateFlow()

    private val mUpdate = MutableStateFlow<ResourceState<Boolean>>(ResourceState.Empty())
    val update = mUpdate.asStateFlow()

    private val mListPriority = MutableStateFlow<List<PriorityModel>>(arrayListOf())
    val listPriority = mListPriority.asStateFlow()

    private val mPriority = MutableStateFlow<Int>(1)
    val priority = mPriority.asStateFlow()

    private val mDate = MutableStateFlow<String>("")
    val date = mDate.asStateFlow()

    private val mTask = MutableStateFlow<ResourceState<TaskModelResponse>>(ResourceState.Empty())
    val task = mTask.asStateFlow()


    fun setSpinner() {
        viewModelScope.launch {
            mListPriority.value = priorityRepository.list()
        }
    }

    fun updateDate(date: String) {
        mDate.value = date
    }

    fun updatePriority(id: Int) {
        mPriority.value = id
    }

    fun saveTask(task: TaskModelResponse) {
        viewModelScope.launch {
            try {
                if(task.id == 0) {
                    mSave.value = taskRepository.save(task)
                } else {
                    mUpdate.value = taskRepository.update(task)
                }
            } catch (t: Throwable) {
                Log.e("ErrorTaskFormVM", t.message.toString())
            }
        }
    }

    fun load(taskId: Int) {
        viewModelScope.launch {
            try {
                mTask.value = taskRepository.task(taskId)
            } catch (t: Throwable) {
                Log.e("ErrorTaskFormVM", t.message.toString())
            }
        }
    }

}