package com.example.tasks.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.databinding.ActivityRegisterBinding
import com.example.tasks.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var mViewModel: RegisterViewModel

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

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
            buttonSave.setOnClickListener {
                val name = editName.text.toString()
                val email = editEmail.text.toString()
                val password = editPassword.text.toString()

                mViewModel.create(name, email, password)
            }
        }
    }
}
