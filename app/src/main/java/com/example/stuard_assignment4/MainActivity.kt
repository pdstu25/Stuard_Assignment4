package com.example.stuard_assignment4

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stuard_assignment4.database.AppDatabase
import com.example.stuard_assignment4.database.Note
import com.example.stuard_assignment4.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MyAdapter
    private val notes = mutableListOf<Note>()
    private val currentTime = LastModified()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.mainActivityRecylerView.setLayoutManager(layoutManager)

        val dividerItemDecoration = DividerItemDecoration(
            applicationContext, layoutManager.getOrientation()
        )
        binding.mainActivityRecylerView.addItemDecoration(dividerItemDecoration)

        adapter = MyAdapter()
        binding.mainActivityRecylerView.setAdapter(adapter)

        loadAllNotes()
    }

    private fun loadAllNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val dao = db.noteDao()
            val results = dao.getAllNotes()

            withContext(Dispatchers.Main) {
                notes.clear()
                notes.addAll(results)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadAllNotesLM() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val dao = db.noteDao()
            val results = dao.orderByLM()

            withContext(Dispatchers.Main) {
                notes.clear()
                notes.addAll(results)
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun loadAllNotesByTitle() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val dao = db.noteDao()
            val results = dao.orderByTitle()

            withContext(Dispatchers.Main) {
                notes.clear()
                notes.addAll(results)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == R.id.addNote_Item) {
            addNewNote()
            return true
        } else if (item.getItemId() == R.id.sortNote_Item) {
            loadAllNotesByTitle()
            return true
        } else if (item.getItemId() == R.id.sortModified_item) {
            loadAllNotesLM()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewNote() {
        val intent = Intent(applicationContext, NoteActivity::class.java)
        intent.putExtra(
            getString(R.string.intent_purpose_key),
            getString(R.string.intent_purpose_add_note)
        )
        startForAddResult.launch(intent)
    }

    private val startForAddResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                loadAllNotes()
            }
        }

    private val startForUpdateResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result : ActivityResult ->

            if (result.resultCode == Activity.RESULT_OK) {
                // alternative - reload the whole database
                // good only for small databases
                loadAllNotes()
            }
        }

    inner class MyViewHolder(val view: TextView) :
        RecyclerView.ViewHolder(view),
        View.OnClickListener, View.OnLongClickListener {

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onClick(view: View?) {
            val intent = Intent(applicationContext, NoteActivity::class.java)

            intent.putExtra(
                "purpose",
                "Update"
            )

            val note = notes[adapterPosition]
            intent.putExtra(
                "note id",
                note.id
            )

            startForUpdateResult.launch(intent)

        }

        override fun onLongClick(view: View?): Boolean {
            val note = notes[adapterPosition]

            val builder = AlertDialog.Builder(view!!.context)
                .setTitle("Confirm delete")
                .setMessage("Are you sure you want to delete " +
                        "${note.title} ${note.notes}?")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok) {
                        dialogInterface, whichButton ->

                    CoroutineScope(Dispatchers.IO).launch {
                        AppDatabase.getDatabase(applicationContext)
                            .noteDao()
                            .deletePerson(note)

                        // alternative - reload the whole database
                        // good only for small databases
                        loadAllNotes()

                    }

                }
            builder.show()

            return true
        }
    }

    inner class MyAdapter :
        RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view, parent, false) as TextView
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val note = notes[position]
            val noteReadUTC = LastModified()

            holder.view.setText(
                "${note.title}, ${noteReadUTC.getUTCDate(note.lastModified)} "
            )
        }

        override fun getItemCount(): Int {
            return notes.size
        }

    }
}