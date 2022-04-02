package com.example.tasks.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.databinding.ActivityLoginBinding
import com.example.tasks.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var mViewModel: LoginViewModel

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Inicializa eventos
        setClickListeners();
        observe()

        // Verifica se usuário está logado
        verifyLoggedUser()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    /**
     * Inicializa os eventos de click
     */
    private fun setClickListeners() {
        binding.buttonLogin.setOnClickListener {
            handleLogin()
        }
        binding.textRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    /**
     * Verifica se usuário está logado
     */
    private fun verifyLoggedUser() {
        mViewModel.verifyLoggedUser()
    }

    /**
     * Observa ViewModel
     */
    private fun observe() {}

    /**
     * Autentica usuário
     */
    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        mViewModel.doLogin(email, password)
    }

}
