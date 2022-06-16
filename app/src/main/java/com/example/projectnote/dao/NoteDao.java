package com.example.projectnote.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projectnote.Entities.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY id DESC")
    List<Note> getAllNotes();

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Query("SELECT * FROM notes where title= :title and subtitle= :subtitle and note_text= :textNote")
    List<Note> checkNote(String title, String subtitle, String textNote);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :title || '%'")
    List<Note> searchNote(String title);
}
