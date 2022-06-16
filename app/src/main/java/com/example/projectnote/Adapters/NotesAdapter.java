package com.example.projectnote.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectnote.Entities.Note;
import com.example.projectnote.R;
import com.example.projectnote.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private List<Note> mListNote;
    private NotesListener notesListener;
//    private Timer timer;
//    private List<Note> notesSource;
//private IClickItemNote iClickItemNote;
//
//    public interface IClickItemNote{
//        void deleteNote(Note note);
//    }

    public NotesAdapter(List<Note> notes, NotesListener notesListener) {
        this.mListNote = notes;
        this.notesListener = notesListener;
//        notesSource = notes;
    }

    public void setData(List<Note> notes) {
        this.mListNote = notes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.setNote(mListNote.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListener.onNoteClicked(mListNote.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListNote.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {

        private TextView textTitle, textSubtitle, textDateTime;
        LinearLayout layoutNote;
//        private Button btnUpdate;
//        private Button btnDelete;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.itemNote);
//            btnUpdate = itemView.findViewById(R.id.btn_update);
//            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        void setNote(Note note) {
            textTitle.setText(note.getTitle());
            if (note.getSubtitle().trim().isEmpty()) {
                textSubtitle.setVisibility(View.GONE);
            } else {
                textSubtitle.setText(note.getSubtitle());
            }

            textDateTime.setText(note.getDateTime());


            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if (note.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
        }

    }

//    public void searchNotes(final String searchKeyword) {
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (searchKeyword.trim().isEmpty()) {
//                    mListNote = notesSource;
//                } else {
//                    ArrayList<Note> temp = new ArrayList<>();
//                    for (Note note : notesSource) {
//                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())) {
//                            temp.add(note);
//                        }
//                    }
//                    mListNote = temp;
//                }
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//                    @Override
//                    public void run() {
//                        notifyDataSetChanged();
//                    }
//                });
//            }
//        }, 500);
//    }

//    public void cancelTimer() {
//        if (timer != null) {
//            timer.cancel();
//        }
//    }

}
