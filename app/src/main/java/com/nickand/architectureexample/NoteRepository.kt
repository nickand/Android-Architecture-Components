package com.nickand.architectureexample

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import com.nickand.architectureexample.db.dao.NoteDao
import com.nickand.architectureexample.db.NoteDatabase
import com.nickand.architectureexample.db.entity.Note


/**
 * Created by Nicolas on 20/10/2018.
 */
class NoteRepository(application: Application) {
    private val noteDao: NoteDao
    private val allNotes: LiveData<List<Note>>

    init {
        val database = NoteDatabase.getInstance(application)
        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    fun insert(note: Note) {
        GeneralAsyncTask(noteDao) { noteDao ->
            noteDao.insert(note)
        }.execute()
    }

    fun update(note: Note) {
        GeneralAsyncTask(noteDao) { noteDao ->
            noteDao.update(note)
        }.execute()
    }

    fun delete(note: Note) {
        GeneralAsyncTask(noteDao) { noteDao ->
            noteDao.delete(note)
        }.execute()
    }

    fun deleteAllNotes() {
        GeneralAsyncTask(noteDao) { noteDao ->
            noteDao.deleteAllNotes()
        }.execute()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }

    private class GeneralAsyncTask internal constructor(val note: NoteDao, val action: (NoteDao) -> Unit) :
            AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg p0: Void?): Void? {
            action(note)
            return null
        }
    }
}