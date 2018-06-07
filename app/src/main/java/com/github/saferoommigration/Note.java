package com.github.saferoommigration;

import android.support.annotation.NonNull;

public class Note {
    private int id;
    @NonNull
    private String text;
    private long timestamp;

    public Note(@NonNull String text) {
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

    public Note(int id, @NonNull String text, long timestamp) {
        this.id = id;
        this.text = text;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Note{[" + id + "]; " + text + '}';
    }
}
