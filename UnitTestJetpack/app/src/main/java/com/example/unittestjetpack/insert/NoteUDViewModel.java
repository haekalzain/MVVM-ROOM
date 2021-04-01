package com.example.unittestjetpack.insert;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.example.unittestjetpack.data.Note;
import com.example.unittestjetpack.data.NotesRepository;

public class NoteUDViewModel extends ViewModel {

    private NotesRepository mNoteRepository;

    public NoteUDViewModel(Application application) {
        mNoteRepository = new NotesRepository(application);
    }

    public void insert(Note note) {
        mNoteRepository.insert(note);
    }

    public void update(Note note) {
        mNoteRepository.update(note);
    }

    public void delete(Note note) {
        mNoteRepository.delete(note);
    }

}
