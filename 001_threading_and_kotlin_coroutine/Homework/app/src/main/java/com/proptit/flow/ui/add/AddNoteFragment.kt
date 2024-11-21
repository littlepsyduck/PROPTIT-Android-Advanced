package com.proptit.flow.ui.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.proptit.flow.R
import com.proptit.flow.data.Note
import com.proptit.flow.databinding.FragmentAddNoteBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AddNoteFragment : Fragment(R.layout.fragment_add_note) {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!
    private val categories = listOf("No category", "Work", "Study", "Entertainment")
    private var time: String? = null
    private var category: String? = null
    private var content: String? = null
    private val args: AddNoteFragmentArgs by navArgs()
    private val addNoteViewModel: AddNoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.category.adapter = adapter

        val id = args.id

        binding.backButton.setOnClickListener {
            back()
        }


        if (id != -1) {
            addNoteViewModel.getNoteById(id)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            addNoteViewModel.noteState.collectLatest { note ->
                note?.let {
                    binding.time.setText(it.time)
                    binding.category.setSelection(setSpinner(it.category))
                    binding.note.setText(it.content)

                    time = it.time
                    category = it.category
                    content = it.content
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            addNoteViewModel.error.collect { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.doneButton.setOnClickListener {
            done(id)
        }

    }

    private fun back(){
        findNavController().popBackStack()
    }

    private fun done(id: Int) {
        val newTime = binding.time.text.toString()
        val newCategory = binding.category.selectedItem.toString()
        val newContent = binding.note.text.toString()

        if (newTime.isBlank() || newCategory.isBlank() || newContent.isBlank()) {
            Toast.makeText(requireContext(), getString("You must fill all the blanks"), Toast.LENGTH_SHORT).show()
            return
        }

        if (id == -1) {
            val newNote = Note(id = 0, content = newContent, category = newCategory, time = newTime)
            addNoteViewModel.insertNote(newNote)
            back()
        } else if (newTime != time || newCategory != category || newContent != content) {
            showConfirmationDialog(newTime, newCategory, newContent, id)
        } else {
            back()
        }
    }

    private fun showConfirmationDialog(newTime: String, newCategory: String, newContent: String, id: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Save")
        builder.setMessage("Do you want to save changes?")
        builder.setPositiveButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.setNegativeButton("OK") { _, _ ->
            val note = Note(id = id, content = newContent, category = newCategory, time = newTime)
            addNoteViewModel.updateNote(note)
            findNavController().popBackStack()
        }
        builder.create().show()
    }



    private fun getString(s: String): String {
        return s
    }

    private fun setSpinner(category: String): Int {
        return categories.indexOf(category).takeIf { it >= 0 } ?: 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}