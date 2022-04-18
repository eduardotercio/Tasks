package com.example.tasks.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tasks.databinding.ActivityLoginBinding
import com.example.tasks.ui.state.ResourceState
import com.example.tasks.ui.viewmodel.LoginViewModel
import com.example.tasks.util.collectLatestStateFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
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
        collector()
        setClickListeners();

        // Mantém os dados dos campos
        //updateFields()

        // Verifica se usuário está logado
        verifyLoggedUser()
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

//    private fun updateFields() {
//        binding.apply {
//            mViewModel.updateFields(editEmail.text.toString(), editPassword.text.toString())
//        }
//    }

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
        if (mViewModel.userIsLogged()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    /**
     * Coleta dados do ViewModel
     */
    private fun collector() {
        collectLatestStateFlow(mViewModel.login) { resource ->
            when (resource) {
                is ResourceState.Sucess -> {
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    Timber.tag("LoginActivity").i("Login feito com sucesso!")
                    finish()
                }
                else -> {
                    mViewModel.toast.collect { message ->
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

//        collectLatestStateFlow(mViewModel.email) {
//            binding.editEmail.setText(it)
//        }
//        collectLatestStateFlow(mViewModel.password) {
//            binding.editPassword.setText(it)
//        }

    }

    /**
     * Autentica usuário
     */
    private fun handleLogin() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()
        mViewModel.doLogin(email, password)
    }


}
