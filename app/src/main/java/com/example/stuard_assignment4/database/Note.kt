package com.example.stuard_assignment4.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Note(
    @PrimaryKey val id: Long,

    @ColumnInfo val title: String,
    @ColumnInfo val notes: String,
    @ColumnInfo(name = "last_modified") val lastModified: String,
) {
}