package com.iti.android_7.presentation.add_note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android_7.data.lacal.room.Note
import com.iti.android_7.data.lacal.room.NoteDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddNoteViewModel : ViewModel() {
    val insertLiveData = MutableLiveData<Boolean>()
    fun saveNote(dao: NoteDAO, note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertNote(note)
            withContext(Dispatchers.Main) {
                insertLiveData.value = true
            }

        }
    }
}