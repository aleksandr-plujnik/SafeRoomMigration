package com.github.saferoommigration.room

import android.content.Context
import com.github.saferoommigration.Note
import com.github.saferoommigration.NotesDatabaseContract

class RoomNotesDatabaseContract(private val context: Context) : NotesDatabaseContract {

    private val notesDatabase by lazy { NotesDatabase.getInstance(context) }

    override fun add(note: Note) {
        notesDatabase.notesDao().addNote(noteEntityFrom(note))
    }

    private fun noteEntityFrom(note: Note) =
            NoteEntity(note.id, note.text, note.timestamp, note.timestamp)

    override fun fetchAll(): MutableList<Note> {
        return notesDatabase.notesDao().fetchAll().map { entity -> Note(entity.id, entity.text, entity.createdAt) }.toMutableList()
    }

    override fun update(note: Note) {
        notesDatabase.notesDao().updateNote(noteEntityFrom(note))
    }

    override fun delete(note: Note) {
        notesDatabase.notesDao().deleteNote(noteEntityFrom(note))
    }

    override fun close() {
        notesDatabase.close()
    }
}