package com.sld.testrecyclerview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sld.testrecyclerview.databinding.ActivityAddNoteBinding

class AddNoteActivity: AppCompatActivity() {
    private val binding: ActivityAddNoteBinding by lazy { ActivityAddNoteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.numberPickerPriority.minValue = 1
        binding.numberPickerPriority.maxValue = 10
        supportActionBar!!.setHomeAsUpIndicator(com.google.android.material.R.drawable.ic_mtrl_chip_close_circle)

        val data = intent.getStringExtra("NOTE_STATE")
        if (data == "Add") {
            title = "Add Note"
        }
        else if (data == "Edit") {
            title = "Edit Note"
            binding.editTextTitle.setText(intent.getStringExtra("title"))
            binding.editTextDescription.setText(intent.getStringExtra("description"))
            binding.numberPickerPriority.value = intent?.getIntExtra("priority", 0)!!
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflate: MenuInflater = menuInflater
        menuInflate.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.save_note -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveNote() {
        val title: String = binding.editTextTitle.text.toString()
        val description: String = binding.editTextDescription.text.toString()
        val priority: Int = binding.numberPickerPriority.value

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title & description", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent(this, MainActivity::class.java).apply {
            putExtra("title", title)
            putExtra("description", description)
            putExtra("priority", priority)

            if (intent.getStringExtra("NOTE_STATE") == "Add") {
                putExtra("NOTE_STATE", "Add")
            }
            else if (intent.getStringExtra("NOTE_STATE") == "Edit") {
                putExtra("id", intent.getIntExtra("id", 0))
                putExtra("NOTE_STATE", "Edit")
            }
        }

        setResult(RESULT_OK, data)
        finish()
    }
}