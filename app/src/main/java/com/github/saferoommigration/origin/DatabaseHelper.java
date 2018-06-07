package com.github.saferoommigration.origin;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.saferoommigration.Note;
import com.github.saferoommigration.NotesDatabaseContract;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public final class DatabaseHelper implements NotesDatabaseContract {
    private static final String TAG = "DatabaseHelper";
    private static final int NB_RETRY_DB_OPEN = 3;
    public static final String PASSW = "awesome_password_42";
    public static final String TABLE_NOTES = "Notes";
    public static final String MAIN_DATABASE_DB = "main_database.db";

    @NonNull
    private final Context applicationContext;
    private InternalHelper helper;
    @Nullable
    private SQLiteDatabase database;

    public DatabaseHelper(@NonNull Context applicationContext) {
        this.applicationContext = applicationContext;
        open();
    }

    private void open() {
        int nbRetry = 0;

        while (nbRetry < NB_RETRY_DB_OPEN) {
            try {
                net.sqlcipher.database.SQLiteDatabase.loadLibs(applicationContext);
                helper = new InternalHelper(applicationContext);
                database = helper.getWritableDatabase(PASSW);
                break;
            } catch (Exception e) {
                if (++nbRetry >= NB_RETRY_DB_OPEN) {
                    Log.e(TAG, "ERROR in DBAdapter.open(): " + e.getMessage(), e);
                } else {
                    Log.e(TAG, "ERROR in DBAdapter.open(): " + e.getMessage() + " - nbRetry: " + nbRetry + " try again", e);
                }
            }
        }
    }

    @Override
    public void close() {
        final InternalHelper internalHelper = this.helper;
        if (internalHelper != null) {
            internalHelper.close();
        }
    }

    @Override
    public void add(@NonNull Note note) {
        ContentValues noteVal = new ContentValues();
        noteVal.put("text", note.getText());
        noteVal.put("timestamp", note.getTimestamp());
        if (database == null) open();
        if (database != null) {
            database.insertWithOnConflict(TABLE_NOTES, null, noteVal, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    @Override
    @NonNull
    public List<Note> fetchAll() {
        List<Note> noteList = new ArrayList<>();
        if (database == null) open();
        if (database != null) {
            Cursor cursor = database.rawQuery("SELECT * FROM Notes", null);
            try {
                final int cursorIndexOfId = cursor.getColumnIndexOrThrow("id");
                final int cursorIndexOfText = cursor.getColumnIndexOrThrow("text");
                final int cursorIndexOfTimestamp = cursor.getColumnIndexOrThrow("timestamp");
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursorIndexOfId);
                    String text = cursor.getString(cursorIndexOfText);
                    long timestamp = cursor.getLong(cursorIndexOfTimestamp);
                    Note note = new Note(id, text, timestamp);
                    noteList.add(note);
                }
            } finally {
                cursor.close();
            }
        }
        return noteList;
    }

    @Override
    public void update(@NonNull Note note) {
        if (note.getId() == 0) {
            add(note);
            return;
        }
        ContentValues noteVal = new ContentValues();
        noteVal.put("text", note.getText());
        noteVal.put("timestamp", note.getTimestamp());
        if (database == null) open();
        if (database != null) {
            database.update(TABLE_NOTES, noteVal, "id = ?", new String[]{String.valueOf(note.getId())});
        }
    }

    @Override
    public void delete(@NonNull Note note) {
        if (note.getId() == 0) return;
        if (database == null) open();
        if (database != null) {
            database.delete(TABLE_NOTES, "id = ?", new String[]{String.valueOf(note.getId())});
        }
    }

    private static class InternalHelper extends SQLiteOpenHelper {

        InternalHelper(@NonNull Context context) {
            super(context, MAIN_DATABASE_DB, null, 2);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE Notes (id INTEGER PRIMARY KEY AUTOINCREMENT, text TEXT NOT NULL, timestamp INTEGER NOT NULL)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1 && newVersion == 2) {
                db.execSQL("ALTER TABLE Notes ADD COLUMN timestamp INTEGER DEFAULT 0");
            }
        }
    }
}
