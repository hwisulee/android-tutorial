package com.sld.testrecyclerview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sld.testrecyclerview.databinding.ActivityMainBinding

class MainActivity: AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var getResult: ActivityResultLauncher<Intent>
    var adapter = NoteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        noteViewModel = ViewModelProvider(this@MainActivity)[NoteViewModel::class.java]
        noteViewModel.getAllNotes().observe(this@MainActivity) { note ->
            val adapter = NoteAdapter()
            val layoutManager = LinearLayoutManager(applicationContext)

            val itemTouchHelperCallback =
                object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: ViewHolder,
                        target: ViewHolder
                    ): Boolean {
                        return true
                    }

                    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                        noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                    }
                }
            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(binding.recyclerView)

            binding.recyclerView.apply {
                this.adapter = adapter
                this.layoutManager = layoutManager

                adapter.onItemClick = {
                    val data = Intent(this@MainActivity, AddNoteActivity::class.java).apply {
                        putExtra("NOTE_STATE", "Edit")
                        putExtra("id", it.id)
                        putExtra("title", it.title)
                        putExtra("description", it.description)
                        putExtra("priority", it.priority)
                    }
                    getResult.launch(data)
                }
                setHasFixedSize(true)
            }

            adapter.submitList(note)
            Log.d("test", "Database size : ${note.size}")
        }

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val data = it.data?.getStringExtra("NOTE_STATE")
                val title = it.data?.getStringExtra("title")
                val description = it.data?.getStringExtra("description")
                val priority = it.data?.getIntExtra("priority", 0)

                if (data == "Add") {
                    val note: Note = Note(null, title, description, priority!!.toInt())
                    noteViewModel.insert(note)
                }
                else if (data == "Edit") {
                    val id = it.data?.getIntExtra("id", 0)
                    val note: Note = Note(id, title, description, priority!!.toInt())
                    noteViewModel.update(note)
                }
            }
        }

        binding.buttonAddNote.setOnClickListener {
            val data = Intent(this@MainActivity, AddNoteActivity::class.java).apply {
                putExtra("NOTE_STATE", "Add")
            }
            getResult.launch(data)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflate: MenuInflater = menuInflater
        menuInflate.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


