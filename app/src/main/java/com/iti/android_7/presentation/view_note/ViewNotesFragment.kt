package com.iti.android_7.presentation.view_note

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.iti.android_7.data.lacal.room.Note
import com.iti.android_7.data.lacal.room.NoteDAO
import com.iti.android_7.data.lacal.room.NoteDatabase
import com.iti.android_7.databinding.FragmentViewNoteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ViewNotesFragment : Fragment() {
    private lateinit var adapter: NoteAdapter
    private val TAG = "ViewNotesFragment"
    private var dao: NoteDAO? = null
    private lateinit var viewModel: ViewNotesViewModel
    private lateinit var binding: FragmentViewNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewNoteBinding.inflate(inflater, container, false)

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
        viewModel = ViewModelProvider(this)[ViewNotesViewModel::class.java]
        setupRecyclerView()
        viewModel.viewNotes(dao)
        observeViewModel()
        binding.fab.setOnClickListener {
            findNavController().navigate(ViewNotesFragmentDirections.actionViewNotesFragmentToAddNoteFragment())
        }
    }

    private fun setupRecyclerView() {
        context?.let {
            binding.rvNotes.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            adapter = NoteAdapter(dao, findNavController(), requireContext())
            adapter.notifyDataSetChanged()
            binding.rvNotes.adapter = adapter
        }
    }

    private fun observeViewModel() {
        viewModel.notesLiveData.observe(viewLifecycleOwner) {

            Log.d("suz", it.toString())

            it?.let { viewData(it) }
            adapter.notifyDataSetChanged()
        }
    }

    private fun viewData(it: List<Note>) {
        if (::adapter.isInitialized) {
            adapter.setData(it)
            adapter.notifyDataSetChanged()

        }
    }
}