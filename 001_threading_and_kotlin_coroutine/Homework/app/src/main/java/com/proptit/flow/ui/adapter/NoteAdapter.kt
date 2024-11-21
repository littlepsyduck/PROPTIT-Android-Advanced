package com.proptit.flow.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.proptit.flow.data.Note
import com.proptit.flow.databinding.ItemNoteBinding

class NoteAdapter(private val noteList: MutableList<Note>, private val onClickItem: (Note) -> Unit) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note){
            binding.time.text = note.time
            binding.category.text = note.category
            binding.content.text = note.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = noteList[position]
        holder.bind(currentNote)
        holder.itemView.setOnClickListener {
            onClickItem(currentNote)
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun setNote(noteList: List<Note>) {
        val result = DiffUtil.calculateDiff(NoteDiffUtil(this.noteList, noteList))
        this.noteList.clear()
        this.noteList.addAll(noteList)
        result.dispatchUpdatesTo(this)
    }

    class NoteDiffUtil(private val oldList: List<Note>, private val newList: List<Note>): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

    }
}