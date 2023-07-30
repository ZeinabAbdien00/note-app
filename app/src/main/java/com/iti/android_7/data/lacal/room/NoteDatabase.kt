package com.iti.android_7.data.lacal.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class], exportSchema = true, version = 1)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getDao(): NoteDAO

    companion object {
        var db: NoteDatabase? = null
        fun buildNoteDb(context: Context): NoteDatabase? {
            db = Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "notes"
            )
                .build()
            return db!!

        }
    }
}