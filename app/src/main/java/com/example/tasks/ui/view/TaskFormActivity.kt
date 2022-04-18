package com.example.tasks.ui.view

import android.app.DatePickerDialog
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.tasks.R
import com.example.tasks.data.model.TaskModelResponse
import com.example.tasks.databinding.ActivityTaskFormBinding
import com.example.tasks.ui.state.ResourceState
import com.example.tasks.ui.viewmodel.TaskFormViewModel
import com.example.tasks.util.collectLatestStateFlow
import com.example.tasks.util.constants.TaskConstants
import com.example.tasks.util.network.NetworkCheck
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TaskFormActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val networkCheck by lazy {
        NetworkCheck(ContextCompat.getSystemService(this, ConnectivityManager::class.java), this)
    }

    private val mViewModel: TaskFormViewModel by viewModels()

    private var _binding: ActivityTaskFormBinding? = null
    private val binding get() = _binding!!

    private val mListPriorityIds: MutableList<Int> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verifica caso a activity tenha sido startada para EDITAR uma task
        loadData()

        // Inicializar coletores
        collector()

        // Inicializar componentes
        mViewModel.setSpinner()

        // Inicializar os clicks
        setClickListeners()


    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun collector() {
        collectLatestStateFlow(mViewModel.listPriority) {
            val list: MutableList<String> = arrayListOf()
            for (item in it) {
                list.add(item.description)
                mListPriorityIds.add(item.id)
            }
            val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
            binding.spinnerPriority.adapter = adapter
        }

        collectLatestStateFlow(mViewModel.date) {
            binding.buttonDate.text = it
        }

        collectLatestStateFlow(mViewModel.priority) {
            binding.spinnerPriority.setSelection(it)
        }

        collectLatestStateFlow(mViewModel.save) {
            when (it) {
                is ResourceState.Sucess -> {
                    Toast.makeText(this, "Tarefa adicionada com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ResourceState.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        collectLatestStateFlow(mViewModel.update) {
            when (it) {
                is ResourceState.Sucess -> {
                    Toast.makeText(this, "Tarefa atualizada com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ResourceState.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        collectLatestStateFlow(mViewModel.task) {
            when (it) {
                is ResourceState.Sucess -> {
                    it.data?.let { task ->
                        binding.apply {
                            editDescription.setText(task.description)
                            spinnerPriority.setSelection(getPriority(task.priorityId))
                            checkComplete.isChecked = task.complete
                            buttonDate.text = task.dueDate
                            buttonSave.text = "Atualizar Tarefa"
                        }
                    }
                }
                is ResourceState.Error -> {
                    Toast.makeText(this, "Erro", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun loadData() {
        val taskId = getExtras()
        if (taskId != null) {
            mViewModel.load(taskId)
        }
    }

    /**
     * Verifica se a Task será Criada ou Atualizada, caso tenha sido passado algo pela intent.
     * É utilizada para passar o ID da Task tanto para carregá-la da API, como para indicar seu ID para atualizar*/
    private fun getExtras(): Int? {
        val bundle = intent.extras
        var taskId: Int? = null
        if (bundle != null) {
            taskId = bundle.getInt(TaskConstants.BUNDLE.TASKID)
        }
        return taskId
    }

    private fun getPriority(priorityId: Int): Int {
        var index = 0
        for (i in 0 until mListPriorityIds.count()) {
            if (mListPriorityIds[i] == priorityId) {
                index = i
                break
            }
        }
        return index
    }

    private fun setClickListeners() {
        binding.apply {
            buttonDate.setOnClickListener {
                datePickerDialog().show()
            }
            buttonSave.setOnClickListener {
                networkCheck.doIfConnected {
                    handleSave()
                }
            }
            spinnerPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    mViewModel.updatePriority(position)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }
    }

    private fun handleSave() {
        binding.apply {
            val description = editDescription.text.toString()
            val priority = mListPriorityIds[spinnerPriority.selectedItemPosition]
            val complete = checkComplete.isChecked
            val dueDate = buttonDate.text.toString()

            val task =
                TaskModelResponse((getExtras() ?: 0), priority, description, dueDate, complete)
            mViewModel.saveTask(task)
        }
    }

    // DatePickerDialog
    private fun datePickerDialog(): DatePickerDialog {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        return DatePickerDialog(this, this, year, month, day)
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val date = SimpleDateFormat("dd/MM/yyyy").format(calendar.time)
        binding.buttonDate.text = date
        mViewModel.updateDate(date)
    }
}
