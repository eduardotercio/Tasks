package com.example.tasks.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.*
import com.example.tasks.R
import com.example.tasks.databinding.ActivityMainBinding
import com.example.tasks.ui.viewmodel.MainViewModel
import com.example.tasks.util.collectLatestStateFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val mViewModel: MainViewModel by viewModels()

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivity(Intent(this, TaskFormActivity::class.java))
        }

        // Navegação
        setupNavigation()

        // Observadores
        collect()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.name()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupNavigation() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_all_tasks, R.id.nav_next_tasks, R.id.nav_expired, R.id.nav_logout),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.nav_logout) {
                mViewModel.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                NavigationUI.onNavDestinationSelected(it, navController)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            true
        }
    }

    private fun collect() {
        collectLatestStateFlow(mViewModel.name) {
            val header = binding.navView.getHeaderView(0)
            header.findViewById<TextView>(R.id.text_name).text = it
        }
    }

}
