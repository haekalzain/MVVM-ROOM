package com.example.unittestjetpack;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.unittestjetpack.data.Note;
import com.example.unittestjetpack.data.NotesRepository;

import java.util.List;

public class NotesAppViewModel extends ViewModel {

    private NotesRepository mNoteRepository;

    public NotesAppViewModel(Application application) {
        mNoteRepository = new NotesRepository(application);
    }

    LiveData<List<Note>> getAllNotes() {
        return mNoteRepository.getAllNotes();
    }
}
