package com.example.unittestjetpack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unittestjetpack.data.Note;
import com.example.unittestjetpack.insert.NoteUDActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class NotesAppActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_app);

        NotesAppViewModel mainViewModel = obtainViewModel(NotesAppActivity.this);
        mainViewModel.getAllNotes().observe(this, noteObserver);

        adapter = new NoteAdapter(NotesAppActivity.this);

        recyclerView = findViewById(R.id.rv_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.fab_add) {
                    Intent intent = new Intent(getApplicationContext(), NoteUDActivity.class);
                    startActivityForResult(intent, NoteUDActivity.REQUEST_ADD);
                }
            }
        });
    }

    private final Observer<List<Note>> noteObserver = new Observer<List<Note>>() {
        @Override
        public void onChanged(@Nullable List<Note> noteList) {
            adapter.setListNotes(noteList);
        }
    };

    @NonNull
    private static NotesAppViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(NotesAppViewModel.class);
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == NoteUDActivity.REQUEST_ADD) {
                if (resultCode == NoteUDActivity.RESULT_ADD) {
                    showSnackbarMessage(getString(R.string.added));
                }
            } else if (requestCode == NoteUDActivity.REQUEST_UPDATE) {
                if (resultCode == NoteUDActivity.RESULT_UPDATE) {
                    showSnackbarMessage(getString(R.string.changed));
                } else if (resultCode == NoteUDActivity.RESULT_DELETE) {
                    showSnackbarMessage(getString(R.string.deleted));
                }
            }
        }
    }
}
