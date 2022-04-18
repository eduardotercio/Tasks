package com.example.tasks.ui.view

import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.tasks.databinding.ActivityRegisterBinding
import com.example.tasks.ui.state.ResourceState
import com.example.tasks.ui.viewmodel.RegisterViewModel
import com.example.tasks.util.network.NetworkCheck
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val networkCheck by lazy {
        NetworkCheck(ContextCompat.getSystemService(this, ConnectivityManager::class.java), this)
    }

    private val mViewModel: RegisterViewModel by viewModels()


    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa eventos
        collector()
        setClickListeners()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun collector() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED){
            mViewModel.create.collectLatest {
                when (it) {
                    is ResourceState.Sucess -> {
                        startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                        finish()
                    }
                    is ResourceState.Error -> {
                        Toast.makeText(this@RegisterActivity, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun setClickListeners() {
        binding.apply {
            buttonSave.setOnClickListener {
                networkCheck.doIfConnected {
                    val name = editName.text.toString()
                    val email = editEmail.text.toString()
                    val password = editPassword.text.toString()

                    mViewModel.create(name, email, password)
                }
            }
        }
    }
}
