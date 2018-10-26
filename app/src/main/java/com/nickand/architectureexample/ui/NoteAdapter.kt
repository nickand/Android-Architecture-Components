package com.nickand.architectureexample.ui

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.nickand.architectureexample.R
import com.nickand.architectureexample.db.entity.Note


/**
 * Created by Nicolas on 22/10/2018.
 */
class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteHolder>(DIFF_CALLBACK) {
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.note_item, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val (title, description, priority) = getItem(position)
        holder.textViewTitle.text = title
        holder.textViewDescription.text = description
        holder.textViewPriority.text = priority.toString()
    }

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    inner class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        val textViewPriority: TextView = itemView.findViewById(R.id.text_view_priority)

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title == newItem.title &&
                        oldItem.description == newItem.description &&
                        oldItem.priority == newItem.priority
            }
        }
    }
}