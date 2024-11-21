package com.proptit.flow.ui.noteList

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.proptit.flow.R
import com.proptit.flow.ui.adapter.NoteAdapter
import com.proptit.flow.databinding.FragmentNoteListBinding
import kotlinx.coroutines.launch

class NoteListFragment : Fragment(R.layout.fragment_note_list) {
    private var _binding: FragmentNoteListBinding? = null
    private val binding get() = _binding!!
    private lateinit var noteAdapter: NoteAdapter
    private val noteListViewModel: NoteListViewModel by viewModels()

    private val filter = listOf("All Notes", "No category", "Work", "Study", "Entertainment")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        val filterAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, filter)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = filterAdapter

        binding.addButton.setOnClickListener {
            findNavController().navigate(NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment(-1))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun init(){
        noteAdapter = NoteAdapter(mutableListOf()){ note ->
            findNavController().navigate(NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment(note.id))
        }
        binding.recyclerView.adapter = noteAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            noteListViewModel.notes.collect{notes ->
                binding.total.text = "Total: ${notes.size}"
                noteAdapter.setNote(notes)
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        noteListViewModel.getAllNotes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}