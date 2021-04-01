package com.example.unittestjetpack.insert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.unittestjetpack.R;
import com.example.unittestjetpack.ViewModelFactory;
import com.example.unittestjetpack.data.Note;
import com.example.unittestjetpack.utils.DateHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteUDActivity extends AppCompatActivity {
    @BindView(R.id.edt_title)
    EditText edtTitle;
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private Note note;
    private int position;

    private NoteUDViewModel noteUDViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_ud);
        ButterKnife.bind(this);
        noteUDViewModel = obtainViewModel(NoteUDActivity.this);
        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        setupContent();
    }

    private void setupContent() {
        if (note != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            note = new Note();
        }

        String actionBarTitle;
        String btnTitle;
        if (isEdit) {
            actionBarTitle = getString(R.string.change);
            btnTitle = getString(R.string.update);

            if (note != null) {
                edtTitle.setText(note.getTitle());
                edtDescription.setText(note.getDesc());
            }
        } else {
            actionBarTitle = getString(R.string.add);
            btnTitle = getString(R.string.save);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        btnSubmit.setText(btnTitle);
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClicked() {
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (title.isEmpty()) {
            edtTitle.setError(getString(R.string.empty));
        } else if (description.isEmpty()) {
            edtDescription.setError(getString(R.string.empty));
        } else {
            note.setTitle(title);
            note.setDesc(description);

            Intent intent = new Intent();
            intent.putExtra(EXTRA_NOTE, note);
            intent.putExtra(EXTRA_POSITION, position);

            if (isEdit) {
                note.setDate(DateHelper.getCurrentDate());
                noteUDViewModel.update(note);
                setResult(RESULT_UPDATE, intent);
            } else {
                note.setDate(DateHelper.getCurrentDate());
                noteUDViewModel.insert(note);
                setResult(RESULT_ADD, intent);
            }
            finish();
        }
    }

    @NonNull
    private static NoteUDViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(NoteUDViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel);
            dialogMessage = getString(R.string.message_cancel);
        } else {
            dialogMessage = getString(R.string.message_delete);
            dialogTitle = getString(R.string.delete);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), (dialog, id) -> {
                    if (!isDialogClose) {
                        noteUDViewModel.delete(note);

                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_POSITION, position);
                        setResult(RESULT_DELETE, intent);

                    }
                    finish();
                })
                .setNegativeButton(getString(R.string.no), (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
