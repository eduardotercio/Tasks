package com.example.tasks.ui.view

import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tasks.databinding.ActivityLoginBinding
import com.example.tasks.ui.state.ResourceState
import com.example.tasks.ui.viewmodel.LoginViewModel
import com.example.tasks.util.collectLatestStateFlow
import com.example.tasks.util.helper.FingerPrintHelper
import com.example.tasks.util.network.NetworkCheck
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.concurrent.Executor

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val mViewModel: LoginViewModel by viewModels()
    private val networkCheck by lazy {
        NetworkCheck(ContextCompat.getSystemService(this, ConnectivityManager::class.java), this)
    }
    private val biometricHelper by lazy {
        FingerPrintHelper(BiometricManager.from(this))
    }

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa eventos
        collector()
        setClickListeners();

        // Verifica se usuário está logado
        verifyLoggedUser()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
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
    }

    /**
     * Inicializa os eventos de click
     */
    private fun setClickListeners() {
        binding.buttonLogin.setOnClickListener {
            networkCheck.doIfConnected {
                handleLogin()
            }
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
            if (biometricHelper.authenticationAvailable()) {
                showAuthentication()
            } else {
                login()
            }
        }
    }

    private fun showAuthentication() {
        val executor: Executor = ContextCompat.getMainExecutor(this)

        val biometricPrompt =
            BiometricPrompt(
                this@LoginActivity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        login()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                    }
                })
        val info = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Title")
            .setSubtitle("Subtitle")
            .setDescription("Description")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(info)
    }

    private fun login() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
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
