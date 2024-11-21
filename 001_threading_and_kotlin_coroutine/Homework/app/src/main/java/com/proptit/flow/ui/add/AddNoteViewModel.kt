package com.proptit.flow.ui.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.proptit.flow.data.Note
import com.proptit.flow.data.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddNoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository = NoteRepository(application)

    private val _noteState = MutableStateFlow<Note?>(null)
    val noteState: StateFlow<Note?> = _noteState

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getNoteById(id: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                noteRepository.getNoteById(id).collect{
                    _noteState.value = it
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch {
            noteRepository.insertNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.updateNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }

    class AddNoteViewModelFactory(private val application: Application): ViewModelProvider
    .Factory{
        override fun <T: ViewModel> create (modelClass: Class<T>): T{
            if(modelClass.isAssignableFrom(AddNoteViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return AddNoteViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}