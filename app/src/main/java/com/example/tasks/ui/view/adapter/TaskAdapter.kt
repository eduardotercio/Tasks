package com.example.tasks.ui.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tasks.R
import com.example.tasks.data.model.TaskModelFragments
import com.example.tasks.databinding.RowTaskListBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: RowTaskListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<TaskModelFragments>() {
        override fun areItemsTheSame(
            oldItem: TaskModelFragments,
            newItem: TaskModelFragments
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: TaskModelFragments,
            newItem: TaskModelFragments
        ): Boolean {
            return oldItem.priority == newItem.priority && oldItem.description == newItem.description &&
                    oldItem.dueDate == newItem.dueDate && oldItem.complete == newItem.complete
        }

    }

    private val differ = AsyncListDiffer(this, differCallback)

    var tasks: List<TaskModelFragments>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    // private var mList: List<TaskModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            RowTaskListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    /**
     *
     * colocar TaskModel como TaskModelResponse(dataClass)
     * criar um TaskModelFragment(dataClass)
     * passar os dados da taskModelResponse para TaskModelFrament*/

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.binding.apply {
            textDescription.text = task.description
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(task.dueDate)
            textDueDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date!!)
            textPriority.text = task.priority
            if (task.complete) {
                textDescription.setTextColor(Color.GRAY)
                imageTask.setImageResource(R.drawable.ic_done)
            } else {
                textDescription.setTextColor(Color.BLACK)
                imageTask.setImageResource(R.drawable.ic_todo)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(task)
            }
        }

        holder.itemView.setOnLongClickListener {
            onLongItemClickListener?.let {
                it(task)
            }
            true
        }

        holder.binding.imageTask.setOnClickListener {
            onCheckClickListener?.let {
                it(task)
            }
        }

    }

    override fun getItemCount() = tasks.size

    private var onItemClickListener: ((TaskModelFragments) -> Unit)? = null
    private var onLongItemClickListener: ((TaskModelFragments) -> Unit)? = null
    private var onCheckClickListener: ((TaskModelFragments) -> Unit)? = null

    fun setOnItemClickListener(listener: (TaskModelFragments) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnLongClickListener(listener: (TaskModelFragments)-> Unit) {
        onLongItemClickListener = listener
    }

    fun setOnCheckClickListener(listener: (TaskModelFragments) -> Unit) {
        onCheckClickListener = listener
    }

}