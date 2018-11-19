package com.nickand.architectureexample.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import com.nickand.architectureexample.R
import kotlinx.android.synthetic.main.activity_add_note.*

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var numberPickerPriority: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        val intent = intent

        if (intent.hasExtra(EXTRA_ID)) {
            title = "Edit Note"
            edit_text_title.setText(intent.getStringExtra(EXTRA_TITLE))
            edit_text_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            number_picker_priority.value = intent.getIntExtra(EXTRA_PRIORITY, 1)
        } else {
            title = "Add Note"
        }
    }

    private fun saveNote() {
        val title = editTextTitle.text.toString()
        val description = editTextDescription.text.toString()
        val priority = numberPickerPriority.value

        if (title.trim().isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)

        val id = intent.getIntExtra(EXTRA_ID, -1)
        if (id != -1) {
            data.putExtra(EXTRA_ID, id)
        }

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save_note -> {
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_ID =
                "com.nickand.architectureexample.EXTRA_ID"
        const val EXTRA_TITLE =
                "com.nickand.architectureexample.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION =
                "com.nickand.architectureexample.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY =
                "com.nickand.architectureexample.EXTRA_PRIORITY"
    }
}
