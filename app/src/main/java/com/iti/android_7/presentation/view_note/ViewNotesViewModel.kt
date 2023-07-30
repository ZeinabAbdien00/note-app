package com.iti.android_7.presentation.view_note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android_7.data.lacal.room.Note
import com.iti.android_7.data.lacal.room.NoteDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewNotesViewModel : ViewModel() {
    val notesLiveData = MutableLiveData<List<Note>>()
    fun viewNotes(dao: NoteDAO?) {
        viewModelScope.launch(Dispatchers.IO) {
            val viewNotes = dao?.viewNotes()
            withContext(Dispatchers.Main) {
                viewNotes?.let {
                    notesLiveData.value = it
                }
            }
        }
    }
}