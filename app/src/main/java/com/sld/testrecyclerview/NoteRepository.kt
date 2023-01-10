package com.sld.testrecyclerview

import android.app.Application
import androidx.lifecycle.LiveData

class NoteRepository(application: Application) {
    private val noteDatabase: NoteDatabase = NoteDatabase.getInstace(application)!!
    private val noteDao: NoteDao = noteDatabase.noteDao()
    private val allNotes: LiveData<List<Note>> = noteDao.getAllnotes()

    fun insert(note: Note) = threadMainController("insert", note)

    fun update(note: Note) = threadMainController("update", note)

    fun delete(note: Note) = threadMainController("delete", note)

    fun deleteAllNotes() {
        try {
            val thread = Thread {
                noteDao.deleteAllNotes()
            }
            thread.start()
        } catch (_: Exception) { }
    }

    fun getAllNotes(): LiveData<List<Note>> = allNotes

    fun threadMainController(code: String, note: Note) {
        try {
            val thread = Thread {
                when(code) {
                    "insert" -> noteDao.insert(note)
                    "update" -> noteDao.update(note)
                    "delete" -> noteDao.delete(note)
                }
            }
            thread.start()
        } catch (_: Exception) { }
    }
}