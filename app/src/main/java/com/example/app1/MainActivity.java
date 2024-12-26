package com.example.app1;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private NoteDBHelper dbHelper;
    private List<Note> notesList;
    private Button addNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new NoteDBHelper(this);
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        addNoteButton = findViewById(R.id.addNoteButton);

        notesList = dbHelper.getAllNotes();
        notesAdapter = new NotesAdapter(notesList);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesRecyclerView.setAdapter(notesAdapter);

        addNoteButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        notesList.clear();
        notesList.addAll(dbHelper.getAllNotes());
        notesAdapter.notifyDataSetChanged();
    }

    // Adapter pour les notes
    public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
        private List<Note> notesList;

        public NotesAdapter(List<Note> notesList) {
            this.notesList = notesList;
        }

        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.note_item, parent, false);
            return new NoteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            Note note = notesList.get(position);
            holder.titleTextView.setText(note.getTitle());
            holder.contentTextView.setText(note.getContent());

            holder.archiveButton.setOnClickListener(v -> {
                dbHelper.archiveNote(note.getId());
                notesList.remove(position);
                notifyItemRemoved(position);
            });

            holder.deleteButton.setOnClickListener(v -> {
                dbHelper.deleteNote(note.getId());
                notesList.remove(position);
                notifyItemRemoved(position);
            });
        }

        @Override
        public int getItemCount() {
            return notesList.size();
        }

        public class NoteViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView, contentTextView;
            Button archiveButton, deleteButton;

            public NoteViewHolder(View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.noteTitleTextView);
                contentTextView = itemView.findViewById(R.id.noteContentTextView);
                archiveButton = itemView.findViewById(R.id.archiveButton);
                deleteButton = itemView.findViewById(R.id.deleteButton);
            }
        }
    }
}
