package com.iti.android_7.data.lacal.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    @ColumnInfo
    var text: String,
    @ColumnInfo
    var date: String,
    @ColumnInfo
    var title: String
)