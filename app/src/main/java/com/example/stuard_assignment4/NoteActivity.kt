package com.example.stuard_assignment4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.stuard_assignment4.database.AppDatabase
import com.example.stuard_assignment4.database.Note
import com.example.stuard_assignment4.databinding.ActivityNoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding

    private var purpose: String? = ""
    private var noteId : Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        purpose = intent.getStringExtra(
            getString(R.string.intent_purpose_key)
        )

        if (purpose.equals(getString(R.string.intent_purpose_update_note))) {
            noteId = intent.getLongExtra(
                getString(R.string.intent_key_note_id),
                -1
            )

            // load exiting person from database
            CoroutineScope(Dispatchers.IO).launch {
                val note = AppDatabase.getDatabase(applicationContext)
                    .noteDao()
                    .getNotes(noteId)

                withContext(Dispatchers.Main) {
                    binding.titleEditText.setText(note.title)
                    binding.editTextTextMultiLine.setText(note.notes)
                }
            }
        }

        setTitle("${purpose} Name(NoteActivity/setTitle)")
    }

    override fun onBackPressed() {
        val noteTitle = binding.titleEditText.getText().toString().trim()
        if (noteTitle.isEmpty()) {
            Toast.makeText(
                applicationContext,
                "Title cannot be empty", Toast.LENGTH_LONG
            ).show()
            return
        }

        var userNotes = binding.editTextTextMultiLine.getText().toString().trim()
        if (userNotes.isEmpty()) {
            userNotes = " "
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val noteDao = AppDatabase.getDatabase(applicationContext)
                .noteDao()
            var currentTime = LastModified()

            if (purpose.equals(getString(R.string.intent_purpose_add_note))) {
                // add the new person to the database
                // when adding, set primary key (id) to 0
                val notes = Note(0, noteTitle, userNotes, "${currentTime.getCurrentTime()}")
                noteId = noteDao.addNote(notes)
                Log.i("STATUS_NAME", "inserted new note: ${notes}")
            } else {
                // update a current person in the database
                val notes = Note(noteId, noteTitle, userNotes, "${currentTime.getCurrentTime()}")
                noteDao.updateNotes(notes)
                Log.i("STATUS_NAME", "updated existing person: ${notes}")
            }

            Log.i("STATUS_NAME", "result_id: ${noteId}")

            val intent = Intent()

            intent.putExtra(
                getString(R.string.intent_key_note_id),
                noteId
            )

            withContext(Dispatchers.Main) {
                setResult(RESULT_OK, intent)
                super.onBackPressed()
            }
        }

    }
}