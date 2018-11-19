package com.nickand.architectureexample.ui

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.nickand.architectureexample.BaseActivity
import com.nickand.architectureexample.R
import com.nickand.architectureexample.db.entity.Note
import com.nickand.architectureexample.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<NoteViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        button_add_note.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        val adapter = NoteAdapter()
        recycler_view.adapter = adapter

        viewModel.getAllNotes().observe(this, Observer {
            //update RecyclerView
            if (it!!.isEmpty()) {
                Toast.makeText(this@MainActivity, "Notes empty", Toast.LENGTH_SHORT).show()
            }
            adapter.submitList(it)
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recycler_view)

        adapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.title)
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.description)
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.priority)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

            val note = Note(title!!, description!!, priority!!)
            viewModel.insert(note)

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            val id = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }

            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

            val note = Note(title!!, description!!, priority!!)
            note.id = id!!
            viewModel.update(note)

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.delete_all_notes -> {
                viewModel.deleteAllNotes()
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModelClass(): Class<NoteViewModel> {
        return NoteViewModel::class.java
    }

    companion object {
        private const val ADD_NOTE_REQUEST = 1
        private const val EDIT_NOTE_REQUEST = 2
    }
}
