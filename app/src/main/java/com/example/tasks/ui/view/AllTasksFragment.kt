package com.example.tasks.ui.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasks.R
import com.example.tasks.util.constants.TaskConstants
import com.example.tasks.databinding.FragmentAllTasksBinding
import com.example.tasks.ui.state.ResourceState
import com.example.tasks.ui.view.adapter.TaskAdapter
import com.example.tasks.ui.viewmodel.AllTasksViewModel
import com.example.tasks.util.collectLatestStateFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllTasksFragment : Fragment() {

    private val mViewModel: AllTasksViewModel by viewModels()
    private val mAdapter by lazy { TaskAdapter() }

    private var _binding: FragmentAllTasksBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentAllTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collector()
        setupRecycler()
        clickAdapter()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.list()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun collector() {
        collectLatestStateFlow(mViewModel.task) { resource ->
            when (resource) {
                is ResourceState.Sucess -> {
                    resource.data?.let { tasks ->
                        if (tasks.count() > 0) {
                            mAdapter.tasks = tasks
                        } else {
                            mAdapter.tasks = arrayListOf()
                        }
                    }
                }
                is ResourceState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Erro ao buscar as tarefas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {}
            }
        }

        collectLatestStateFlow(mViewModel.check) { resource ->
            when (resource) {
                is ResourceState.Sucess -> {
                    mViewModel.list()
                }
                is ResourceState.Error -> {
                    Toast.makeText(context, "Erro ao atualizar tarefa.", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }

        collectLatestStateFlow(mViewModel.delete) { resource ->
            when (resource) {
                is ResourceState.Sucess -> {
                    mViewModel.list()
                }
                is ResourceState.Error -> {
                    Toast.makeText(context, "Erro ao deletar tarefa.", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun setupRecycler() = with(binding) {
        recyclerAllTasks.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun clickAdapter() {
        mAdapter.apply {
            setOnItemClickListener { task ->
                val intent = Intent(context, TaskFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(TaskConstants.BUNDLE.TASKID, task.id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            setOnCheckClickListener { task ->
                mViewModel.check(task.id, task.complete)
            }

            setOnLongClickListener { task ->
                AlertDialog.Builder(context)
                    .setTitle(R.string.remocao_de_tarefa)
                    .setMessage(R.string.remover_tarefa)
                    .setPositiveButton(R.string.sim) { dialog, which ->
                        mViewModel.delete(task.id)
                    }
                    .setNeutralButton(R.string.cancelar, null)
                    .show()
                true
            }
        }
    }

}
