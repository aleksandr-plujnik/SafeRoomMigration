package com.github.saferoommigration.room

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.github.saferoommigration.origin.DatabaseHelper.TABLE_NOTES

@Entity(tableName = TABLE_NOTES)
data class NoteEntity(@PrimaryKey(autoGenerate = true) val id: Int,
                      @ColumnInfo(name = "text") val text: String,
                      @ColumnInfo(name = "created_at") val createdAt: Long,
                      @ColumnInfo(name = "updated_at") val updatedAt: Long)
