package com.example.stuard_assignment4.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note")
    fun getAllNotes() : List<Note>
}