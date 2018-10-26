package com.nickand.architectureexample.db

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.os.AsyncTask
import com.nickand.architectureexample.db.entity.Note
import com.nickand.architectureexample.db.dao.NoteDao


/**
 * Created by Nicolas on 20/10/2018.
 */
@Database(entities = arrayOf(Note::class), version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    private class PopulateDbAsyncTask internal constructor(db: NoteDatabase) : AsyncTask<Void, Void, Void>() {
        private val noteDao: NoteDao = db.noteDao()

        override fun doInBackground(vararg voids: Void): Void? {
            //Fill database
            /*noteDao.insert(Note("Title 1", "Description 1", 1))
            noteDao.insert(Note("Title 2", "Description 2", 2))
            noteDao.insert(Note("Title 3", "Description 3", 3))*/
            return null
        }
    }

    companion object {

        private var instance: NoteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        NoteDatabase::class.java, "note_database")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
            }
            return instance as NoteDatabase
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                instance?.let { PopulateDbAsyncTask(it).execute() }
            }
        }
    }
}