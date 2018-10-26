package com.nickand.architectureexample.db.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.nickand.architectureexample.db.entity.Note

/**
 * Created by Nicolas on 20/10/2018.
 */
@Dao
interface NoteDao {

    @Insert
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("DELETE FROM note_table")
    fun deleteAllNotes()

    @Query("SELECT * FROM note_table ORDER BY priority DESC")
    fun getAllNotes(): LiveData<List<Note>>
}