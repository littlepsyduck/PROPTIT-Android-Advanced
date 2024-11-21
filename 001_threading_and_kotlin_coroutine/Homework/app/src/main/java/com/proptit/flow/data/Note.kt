package com.proptit.flow.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "time") val time: String
    )