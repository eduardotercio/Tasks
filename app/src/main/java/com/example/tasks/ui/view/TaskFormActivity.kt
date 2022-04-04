package com.example.tasks.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.databinding.ActivityTaskFormBinding
import com.example.tasks.ui.viewmodel.TaskFormViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFormActivity : AppCompatActivity() {

    private lateinit var mViewModel: TaskFormViewModel

    private var _binding: ActivityTaskFormBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Inicializa eventos
        setClickListeners()
        observe()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun observe() {
    }

    private fun setClickListeners() {
        binding.apply {

        }
    }
}
