package com.iti.android_7.presentation.add_note


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iti.android_7.data.lacal.room.Note
import com.iti.android_7.data.lacal.room.NoteDAO
import com.iti.android_7.data.lacal.room.NoteDatabase
import com.iti.android_7.databinding.FragmentAddNoteBinding
import com.iti.android_7.utils.DateTimeManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AddNoteFragment : Fragment() {
    private var dao: NoteDAO? = null
    private lateinit var binding: FragmentAddNoteBinding
    private lateinit var viewModel: AddNoteViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)
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
        viewModel = ViewModelProvider(this)[AddNoteViewModel::class.java]
        observeViewModel()
        binding.btnSaveNote.setOnClickListener {

            if (binding.etNoteText.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "No Note Entered", Toast.LENGTH_SHORT).show()
            } else {
                val text = binding.etNoteText.text.toString()
                val title = binding.etNoteTitle.text.toString()
                if (dao != null)
                    viewModel.saveNote(
                        dao!!,
                        Note(text = text, date = DateTimeManager.currentDateTime(), title = title)
                    )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.insertLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Note Saved", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }
}