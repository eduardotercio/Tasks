package com.example.tasks.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.data.model.TaskModelFragments
import com.example.tasks.data.model.TaskModelResponse
import com.example.tasks.repository.PriorityRepository
import com.example.tasks.repository.TaskRepository
import com.example.tasks.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val priorityRepository: PriorityRepository
) : ViewModel() {

    private val mTask =
        MutableStateFlow<ResourceState<List<TaskModelFragments>>>(ResourceState.Empty())
    val task = mTask.asStateFlow()

    private val mCheck = MutableStateFlow<ResourceState<Boolean>>(ResourceState.Empty())
    val check = mCheck.asStateFlow()

    private val mDelete = MutableStateFlow<ResourceState<Boolean>>(ResourceState.Empty())
    val delete = mDelete.asStateFlow()

    /**
     * Converte os models para que possamos ter acesso à prioridade e não ao seu ID*/
    private suspend fun convert(response: ResourceState<List<TaskModelResponse>>): ResourceState<List<TaskModelFragments>> {
        val newList: MutableList<TaskModelFragments> = arrayListOf()
        response.data?.let { list ->
            for (l in list) {
                val id = l.id
                val description = l.description
                val priority = priorityRepository.priority(l.priorityId)
                val complete = l.complete
                val dueDate = l.dueDate
                newList.add(TaskModelFragments(id, priority, description, dueDate, complete))
            }
        }
        return ResourceState.Sucess(newList)
    }

    /**
     * GET*/
    fun list(taskFilter: Int) {
        viewModelScope.launch {
            MainScope().launch {
                withContext(Dispatchers.Default) {
                    try {
                        when (taskFilter) {
                            0 -> {
                                mTask.value = convert(taskRepository.list())
                            }
                            1 -> {
                                mTask.value = convert(taskRepository.listNextWeek())
                            }
                            else -> {
                                mTask.value = convert(taskRepository.listOverdue())
                            }
                        }
                    } catch (t: Throwable) {
                        mTask.value = ResourceState.Error(t.message)
                        Log.e("ErrorAllTasksVM", t.message.toString())
                    }
                }
            }
        }
    }

    fun check(id: Int, complete: Boolean) {
        viewModelScope.launch {
            try {
                mCheck.value = taskRepository.updateStatus(id, complete)
            } catch (t: Throwable) {
                Log.e("ErrorAllTasksVM", t.message.toString())
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            try {
                mDelete.value = taskRepository.delete(id)
            } catch (t: Throwable) {
                Log.e("ErrorAllTasksVM", t.message.toString())
            }
        }
    }

}