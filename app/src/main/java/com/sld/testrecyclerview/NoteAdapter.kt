package com.sld.testrecyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sld.testrecyclerview.databinding.NoteItemBinding

class NoteAdapter() : ListAdapter<Note, NoteAdapter.ViewHolder>(diffUtil) {
    var onItemClick: ((Note) -> Unit)? = null

    inner class ViewHolder(private val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(note: Note) {
            with(binding) {
                binding.textViewPrioity.text = note.priority.toString()
                binding.textViewTitle.text = note.title
                binding.textViewDescription.text = note.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.ViewHolder {
        val view = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    companion object {
        val diffUtil = object: DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
}