package com.github.saferoommigration.room

import android.arch.persistence.room.*

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNote(noteEntity: NoteEntity)

    @Query("SELECT * FROM Notes")
    fun fetchAll(): List<NoteEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNote(noteEntity: NoteEntity)

    @Delete
    fun deleteNote(noteEntity: NoteEntity)

}