package com.github.saferoommigration;

import android.support.annotation.NonNull;

import java.util.List;

public interface NotesDatabaseContract {

    void add(@NonNull Note note);

    @NonNull
    List<Note> fetchAll();

    void update(@NonNull Note note);

    void delete(@NonNull Note note);

    void close();
}
