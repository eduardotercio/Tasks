package com.example.tasks.ui.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import com.example.tasks.R
import com.example.tasks.data.model.TaskModel
import com.example.tasks.databinding.ActivityTaskFormBinding
import com.example.tasks.ui.state.ResourceState
import com.example.tasks.ui.viewmodel.TaskFormViewModel
import com.example.tasks.util.collectLatestStateFlow
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TaskFormActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val mViewModel: TaskFormViewModel by viewModels()

    private var _binding: ActivityTaskFormBinding? = null
    private val binding get() = _binding!!

    private val mListPriorityIds: MutableList<Int> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                else -> {

                }
            }
        }
    }

    private fun setClickListeners() {
        binding.apply {
            buttonDate.setOnClickListener {
                datePickerDialog().show()
            }
            buttonSave.setOnClickListener {
                handleSave()
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

            val task = TaskModel(0, priority, description, dueDate, complete)
            mViewModel.saveTask(task)
        }
    }

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
