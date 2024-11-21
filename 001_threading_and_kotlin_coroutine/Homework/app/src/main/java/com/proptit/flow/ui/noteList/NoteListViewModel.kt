package com.proptit.flow.ui.noteList

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

class NoteListViewModel(application: Application) : AndroidViewModel(application) {

    private val noteRepository = NoteRepository(application)
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Function to get all notes
    fun getAllNotes() {
        viewModelScope.launch {
            _loading.value = true
            try {
                noteRepository.getAllNote().collect{
                    _notes.value = it
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    class NoteListViewModelFactory(private val application: Application): ViewModelProvider
    .Factory{
        override fun <T: ViewModel> create (modelClass: Class<T>): T{
            if(modelClass.isAssignableFrom(NoteListViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return NoteListViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}