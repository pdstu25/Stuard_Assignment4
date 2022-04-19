package com.example.stuard_assignment4.database

import androidx.room.*

@Dao
interface NoteDao {

    /**
     * @param note the Note object to insert. The
     *      id should be 0
     *
     * @return the id of the note object just inserted
     */
    @Insert
    fun addNote(note : Note) : Long

    /**
     * @param notes the Note object to update. The
     *      id CANNOT be 0
     */
    @Update
    fun updateNotes(note : Note)

    @Delete
    fun deletePerson(note: Note)

    /**
     * Deletes EVERYTHING in the table
     */
    @Query("DELETE FROM Note")
    fun deleteAllNotes()

    @Query("SELECT * FROM note")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM note WHERE id = :noteId")
    fun getNotes(noteId : Long) : Note

    @Query("SELECT * FROM note ORDER BY last_modified DESC")
    fun orderByLM() : List<Note>

    @Query("SELECT * FROM note ORDER BY title")
    fun orderByTitle(): List<Note>
}