package com.iti.android_7.presentation.edit_note

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android_7.data.lacal.room.Note
import com.iti.android_7.data.lacal.room.NoteDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditNoteViewModel : ViewModel() {

    val editLiveData = MutableLiveData<Boolean>()
    fun editNote(dao: NoteDAO?, note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            dao?.updateNote(note)
            withContext(Dispatchers.Main) {
                editLiveData.value = true
            }

        }
    }

}