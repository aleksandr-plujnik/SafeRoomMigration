package com.github.saferoommigration;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder> {

    private final List<Note> noteList;

    public NotesAdapter(@NonNull List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteHolder(parent, R.layout.note_item);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        holder.applyNote(getItem(position));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public Note getItem(int position) {
        return noteList.get(position);
    }

    @NonNull
    public List<Note> getNoteList() {
        return noteList;
    }

    public void notifyItemLastInserted() {
        int lastInsertedIndex = getItemCount() - 1;
        notifyItemInserted(lastInsertedIndex);
    }

    static class NoteHolder extends RecyclerView.ViewHolder {

        public NoteHolder(ViewGroup viewGroup, @LayoutRes int layoutId) {
            super(createView(viewGroup, layoutId));
        }

        void applyNote(@NonNull Note note) {
            ((TextView) itemView).setText(note.getText());
        }

        private static View createView(ViewGroup viewGroup, @LayoutRes int id) {
            return LayoutInflater.from(viewGroup.getContext()).inflate(id, viewGroup, false);
        }
    }
}
