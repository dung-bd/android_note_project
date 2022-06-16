package com.example.projectnote.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.projectnote.Adapters.NotesAdapter;
import com.example.projectnote.Entities.Note;
import com.example.projectnote.R;
import com.example.projectnote.database.NotesDatabase;
import com.example.projectnote.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesListener {

    public static final int KEY_CODE = 1;
    public static final int UPDATE_CODE = 2;
    public static final int SHOW_CODE = 3;


    private RecyclerView notesRecycleView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;
    EditText inputSearch;
    private int noteClickedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                startActivityForResult(intent, KEY_CODE);
            }
        });

        notesRecycleView = findViewById(R.id.notesRecycleView);
        noteList = new ArrayList<>();

        notesAdapter = new NotesAdapter(noteList, this);
        notesAdapter.setData(noteList);
        notesRecycleView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        notesRecycleView.setAdapter(notesAdapter);
        loadData();

        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //Logic
                    handleSearchNote();
                }
                return false;
            }
        });
//        inputSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                notesAdapter.cancelTimer();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if(noteList.size() != 0){
//                    notesAdapter.searchNotes(s.toString());
//                }
//            }
//        });
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, UPDATE_CODE);
    }

    private void loadData() {
        noteList = NotesDatabase.getInstance(this).noteDao().getAllNotes();
        notesAdapter.setData(noteList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_CODE && resultCode == RESULT_OK) {
            loadData();
        } else if (requestCode == UPDATE_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                loadData();
            }
        }
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void handleSearchNote() {
        String strKeyword = inputSearch.getText().toString().trim();
        noteList = new ArrayList<>();
        noteList = NotesDatabase.getInstance(this).noteDao().searchNote(strKeyword);
        notesAdapter.setData(noteList);
        hideSoftKeyboard();
    }

}