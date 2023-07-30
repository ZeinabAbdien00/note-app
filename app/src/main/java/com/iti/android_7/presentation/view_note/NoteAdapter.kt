package com.iti.android_7.presentation.view_note

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.android_7.R
import com.iti.android_7.data.lacal.room.Note
import com.iti.android_7.data.lacal.room.NoteDAO
import com.iti.android_7.databinding.NoteRowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


class NoteAdapter(val dao: NoteDAO?, val navController: NavController, val context: Context) :
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private var list = arrayListOf<Note>()
    val colorList = arrayOf(
        "R.color.dark_base",
        "R.color.base",
        "R.color.purple_2",
        "R.color.purple_3",
        "R.color.purple_4",
        "R.color.purple_5",
        "R.color.purple_200",
    )

    inner class NoteViewHolder(binding: NoteRowBinding) : ViewHolder(binding.root) {
        private val noteText = binding.lbNoteText
        private val noteDate = binding.lbNoteDate
        private val noteTitle = binding.tvNoteTitle
        private val deleteNote = binding.btnDeleteNote
        private val editNote = binding.btnEditNote
        private val view = binding.root
        fun bind(note: Note) {

            if (note.title.isEmpty())
                noteTitle.text = "No Title"
            else
                noteTitle.text = note.title

            if (note.text.isEmpty())
                noteText.text = "No Text"
            else
                noteText.text = note.text

            noteDate.text = note.date

            val color = getRandomColorFromList(colorList)
            Log.d("suzan", color)

            val background = when (color) {

                "R.color.dark_base" -> R.color.dark_base
                "R.color.base" -> R.color.base
                "R.color.purple_2" -> R.color.purple_2
                "R.color.purple_3" -> R.color.purple_3
                "R.color.purple_4" -> R.color.purple_4
                "R.color.purple_5" -> R.color.purple_5
                "R.color.purple_200" -> R.color.purple_200
                else -> R.color.base_black
            }

            view.setCardBackgroundColor(ContextCompat.getColor(context, background))

            val editDrawable = ContextCompat.getDrawable(context, R.drawable.edit_note)
            DrawableCompat.setTint(editDrawable!!, ContextCompat.getColor(context, background))
            editNote.setCompoundDrawablesRelativeWithIntrinsicBounds(null, editDrawable, null, null)

            val deleteDrawable = ContextCompat.getDrawable(context, R.drawable.delete)
            DrawableCompat.setTint(deleteDrawable!!, ContextCompat.getColor(context, R.color.red))
            deleteNote.setCompoundDrawablesRelativeWithIntrinsicBounds(null, deleteDrawable, null, null)

        }

        private fun getRandomColorFromList(colorList: Array<String>): String {
            return colorList[Random.nextInt(colorList.size)].toString()
        }


        fun onEvent(note: Note) {
            //call edit
            editNote.setOnClickListener {
                navController.navigate(
                    ViewNotesFragmentDirections.actionViewNotesFragmentToEditNoteFragment(
                        note = note.text,
                        id = note.id ?: 0,
                        title = note.title
                    )
                )
            }

            //call delete
            deleteNote.setOnClickListener {

                showDialog(
                    message = "Are you sure you want to delete this note?",
                    positiveTxt = "yes",
                    negativeTxt = "No",
                    logic = deleteNoteLogic,
                    note = note
                )


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(
            NoteRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = list[position]
        holder.bind(note)
        holder.onEvent(note)
    }

    fun setData(notes: List<Note>) {
        list.clear()
        list.addAll(notes)
        notifyDataSetChanged()
    }

    private fun showDialog(
        message: String,
        positiveTxt: String,
        negativeTxt: String,
        logic: (note: Note) -> Unit,
        note: Note
    ) {
        MaterialAlertDialogBuilder(context, R.style.AlertDialogCustom)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(positiveTxt) { _, _ ->
                logic(note)
            }
            .setNegativeButton(negativeTxt) { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true)
            .show()

    }

    private val deleteNoteLogic: (note: Note) -> Unit = {
        deleteNote(it)
    }

    private fun deleteNote(note: Note) {
        CoroutineScope(Dispatchers.Main).launch {
            dao?.deleteNote(note)
            list.remove(note)
            notifyDataSetChanged()
            Toast.makeText(context, "Note Deleted", Toast.LENGTH_SHORT).show()

        }
    }

}