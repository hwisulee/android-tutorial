package com.sld.testrecyclerview

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application): AndroidViewModel(application) {
    private val repository = NoteRepository(application)
    private val note = repository.getAllNotes()

    fun insert(note: Note) = repository.insert(note)

    fun update(note: Note) = repository.update(note)

    fun delete(note: Note) = repository.delete(note)

    fun deleteAllNotes() = repository.deleteAllNotes()

    fun getAllNotes(): LiveData<List<Note>> = this.note
}