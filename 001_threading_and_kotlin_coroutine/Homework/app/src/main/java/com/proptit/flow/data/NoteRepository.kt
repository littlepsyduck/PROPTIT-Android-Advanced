package com.proptit.flow.data

import android.app.Application
import kotlinx.coroutines.flow.Flow

class NoteRepository(application: Application) {
    private val noteDAO : NoteDAO = NoteDatabase.getDatabase(application).noteDao()

    fun getAllNote(): Flow<List<Note>>{
        return noteDAO.getAllNotes()
    }

    suspend fun insertNote(note: Note) {
        noteDAO.insertNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDAO.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDAO.deleteNote(note)
    }

    fun getNoteById(id: Int): Flow<Note> {
        return noteDAO.getNoteById(id)
    }

}