package com.iti.android_7.presentation.edit_note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.iti.android_7.R
import com.iti.android_7.data.lacal.room.Note
import com.iti.android_7.data.lacal.room.NoteDAO
import com.iti.android_7.data.lacal.room.NoteDatabase
import com.iti.android_7.databinding.FragmentEditNoteBinding
import com.iti.android_7.presentation.view_note.ViewNotesViewModel
import com.iti.android_7.utils.DateTimeManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class EditNoteFragment : Fragment() {

    private lateinit var binding: FragmentEditNoteBinding
    private var dao: NoteDAO? = null
    private lateinit var viewModel: EditNoteViewModel
    private val args: EditNoteFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEditNoteBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[EditNoteViewModel::class.java]
        binding.etUpdateNoteText.setText(args.note.toString())
        binding.etUpdateNoteTitle.setText(args.title.toString())
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { context ->
            GlobalScope.launch(Dispatchers.IO) {
                val db = NoteDatabase.buildNoteDb(context)
                dao = db?.getDao()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdateNote.setOnClickListener {

            if (binding.etUpdateNoteText.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "No Note Entered", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.editNote(
                    dao = dao,
                    Note(
                        id = args.id,
                        text = binding.etUpdateNoteText.text.toString(),
                        date = DateTimeManager.currentDateTime(),
                        title = binding.etUpdateNoteTitle.text.toString()
                    )
                )
                Toast.makeText(requireContext(), "Note Edited", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }


    }

}