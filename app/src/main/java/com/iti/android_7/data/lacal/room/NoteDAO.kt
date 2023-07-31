package com.iti.android_7.data.lacal.room

import androidx.room.*

@Dao
interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: Note)

    @Query("select * from Note")
    fun viewNotes(): List<Note>

    @Delete
    suspend fun deleteNote(note: Note)

    @Update
    fun updateNote(note: Note)


}