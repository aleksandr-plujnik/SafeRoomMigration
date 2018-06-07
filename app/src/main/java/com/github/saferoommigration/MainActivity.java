package com.github.saferoommigration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.github.saferoommigration.origin.DatabaseHelper;
import com.github.saferoommigration.room.RoomNotesDatabaseContract;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NotesDatabaseContract notesDatabaseContract;
    private EditText noteEditText;
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteEditText = findViewById(R.id.etNoteEdit);
        findViewById(R.id.btNoteSave).setOnClickListener(this);
        //step to reproduce.
        //1. uncomment first initialization below and run the app (db version 2)
//        notesDatabaseContract = new DatabaseHelper(getApplicationContext()); // initial case
        //2. comment one line above and uncomment line below and run the app (db version 3 with room migration)
//        notesDatabaseContract = new RoomNotesDatabaseContract(getApplicationContext()); //case with WAL exception
        initNotesList();
    }

    private void initNotesList() {
        RecyclerView recyclerViewNotes = findViewById(R.id.rvNoteItems);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        List<Note> noteList = notesDatabaseContract.fetchAll();
        notesAdapter = new NotesAdapter(noteList);
        recyclerViewNotes.setAdapter(notesAdapter);
    }

    @Override
    public void onClick(View v) {
        if (noteEditText != null && noteEditText.getText() != null) {
            String message = noteEditText.getText().toString();
            Note note = new Note(message);
            notesDatabaseContract.add(note);
            notesAdapter.getNoteList().add(note);
            notesAdapter.notifyItemLastInserted();
            noteEditText.setText(null);
        }
    }

    @Override
    protected void onStop() {
        notesDatabaseContract.close();
        super.onStop();
    }
}
