package com.example.unittestjetpack;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.unittestjetpack.insert.NoteUDViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelFactory INSTANCE;

    private final Application mApplication;

    private ViewModelFactory(Application application) {
        mApplication = application;
    }

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application);
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NotesAppViewModel.class)) {
            return (T) new NotesAppViewModel(mApplication);
        } else if (modelClass.isAssignableFrom(NoteUDViewModel.class)) {
            return (T) new NoteUDViewModel(mApplication);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
