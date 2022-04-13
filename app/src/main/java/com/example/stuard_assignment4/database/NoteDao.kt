package com.example.stuard_assignment4.database

import androidx.room.Dao

@Dao
interface NoteDao {

    fun getAllNotes() : List<Note>
}