package com.nickand.architectureexample

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Nicolas on 20/10/2018.
 */
@Entity(tableName = "note_table")
data class Note(val title: String, val description: String, val priority: Int) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}