package com.example.tasks.ui.view

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.databinding.ActivityTaskFormBinding
import com.example.tasks.ui.viewmodel.TaskFormViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TaskFormActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: TaskFormViewModel

    private var _binding: ActivityTaskFormBinding? = null
    private val binding get() = _binding!!

    private val mDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Inicializa eventos
        setClickListeners()
        collector()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun collector() {
    }

    private fun setClickListeners() {
        binding.apply {
            buttonDate.setOnClickListener {
                setDialog()
            }
        }
    }

    private fun setDialog() {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        DatePickerDialog(this, this, year, month, day).show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val date = mDateFormat.format(calendar.time)
        binding.buttonDate.text = date
    }
}
