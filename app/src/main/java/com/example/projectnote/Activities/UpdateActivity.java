package com.example.projectnote.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectnote.Entities.Note;
import com.example.projectnote.R;
import com.example.projectnote.database.NotesDatabase;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {
    private EditText inputNoteTitle, inputNoteSubtitle, inputNoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator;

    private String selectedNoteColor;

    private ImageView imageSave, imageDelete;
    private List<Note> mListNote;
    private AlertDialog dialogDeleteNote;

    private Note alreadyAvailableNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteText = findViewById(R.id.inputNote);
        textDateTime = findViewById(R.id.textDateTime);
        imageSave = findViewById(R.id.imageSave);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        imageDelete = findViewById(R.id.btndelete);


        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();

            }
        });
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote();
            }

        });

        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );

        selectedNoteColor = "#333333";

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

        initMiscellaneous();
        setSubtitleIndicatorColor();

    }

    private void setViewOrUpdateNote() {
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        textDateTime.setText(alreadyAvailableNote.getDateTime());
    }

    private void saveNote() {
        String strInputNoteTitle = inputNoteText.getText().toString().trim();
        String strInputNoteSubtitle = inputNoteSubtitle.getText().toString().trim();
        String strInputNoteText = inputNoteText.getText().toString().trim();
        String strDateTime = textDateTime.getText().toString().trim();

        if (TextUtils.isEmpty(strInputNoteText) || TextUtils.isEmpty(strInputNoteTitle) || TextUtils.isEmpty(strInputNoteSubtitle)) {
            Toast.makeText(this, "Please fill in these fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note(strInputNoteTitle, strDateTime, strInputNoteSubtitle, strInputNoteText);
        note.setColor(selectedNoteColor);
        note.setTitle(strInputNoteTitle);
        note.setSubtitle(strInputNoteSubtitle);
        note.setNoteText(strInputNoteText);

        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
        }

        NotesDatabase.getInstance(this).noteDao().updateNote(note);
        hideSoftKeyboard();
        Toast.makeText(this, "Update note successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();


    }

    private void deleteNote() {
        String strInputNoteTitle = inputNoteText.getText().toString().trim();
        String strInputNoteSubtitle = inputNoteSubtitle.getText().toString().trim();
        String strInputNoteText = inputNoteText.getText().toString().trim();
        String strDateTime = textDateTime.getText().toString().trim();

        Note note = new Note(strInputNoteTitle, strDateTime, strInputNoteSubtitle, strInputNoteText);
        NotesDatabase.getInstance(this).noteDao().deleteNote(note);
        Toast.makeText(this, "Delete note successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    private boolean isNoteExist(Note note) {
        List<Note> list = NotesDatabase.getInstance(this).noteDao().checkNote(note.getTitle(), note.getSubtitle(), note.getNoteText());
        return list != null && !list.isEmpty();
    }

    public void hideSoftKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        final ImageView imagecolor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imagecolor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imagecolor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imagecolor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imagecolor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333";
                imagecolor1.setImageResource(R.drawable.ic_done);
                imagecolor2.setImageResource(0);
                imagecolor3.setImageResource(0);
                imagecolor4.setImageResource(0);
                imagecolor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                imagecolor1.setImageResource(0);
                imagecolor2.setImageResource(R.drawable.ic_done);
                imagecolor3.setImageResource(0);
                imagecolor4.setImageResource(0);
                imagecolor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imagecolor1.setImageResource(0);
                imagecolor2.setImageResource(0);
                imagecolor3.setImageResource(R.drawable.ic_done);
                imagecolor4.setImageResource(0);
                imagecolor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A52FC";
                imagecolor1.setImageResource(0);
                imagecolor2.setImageResource(0);
                imagecolor3.setImageResource(0);
                imagecolor4.setImageResource(R.drawable.ic_done);
                imagecolor5.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imagecolor1.setImageResource(0);
                imagecolor2.setImageResource(0);
                imagecolor3.setImageResource(0);
                imagecolor4.setImageResource(0);
                imagecolor5.setImageResource(R.drawable.ic_done);
                setSubtitleIndicatorColor();
            }
        });
        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && alreadyAvailableNote.getColor().trim().isEmpty()) {
            switch (alreadyAvailableNote.getColor()) {
                case "#FDBE3B":
                    layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#FF4842":
                    layoutMiscellaneous.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#3A52Fc":
                    layoutMiscellaneous.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#000000":
                    layoutMiscellaneous.findViewById(R.id.viewColor5).performClick();
                    break;
            }
        }
        if (alreadyAvailableNote != null) {
            layoutMiscellaneous.findViewById(R.id.layoutDeleteCode).setVisibility(View.VISIBLE);
            layoutMiscellaneous.findViewById(R.id.layoutDeleteCode).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteNoteDialog();
                }
            });
        }
    }

    private void showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null) {
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTask extends AsyncTask<Void, Void, Void> {

                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDatabase.getInstance(getApplicationContext()).noteDao()
                                    .deleteNote(alreadyAvailableNote);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                    new DeleteNoteTask().execute();

                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteNote.dismiss();
                }
            });

        }

        dialogDeleteNote.show();

    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }

}