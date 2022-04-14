package com.example.stuard_assignment4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stuard_assignment4.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}