package com.example.projectnote.listeners;

import com.example.projectnote.Entities.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
