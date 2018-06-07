package aleksandrkim.ArchComponentsTest.NoteDetails;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import aleksandrkim.ArchComponentsTest.HostActivity.NavigationActivity;
import aleksandrkim.ArchComponentsTest.R;
import aleksandrkim.ArchComponentsTest.Utils.Colors;

public class NoteDetailsFragment extends Fragment implements NavigationActivity.BackEnabled {
    public static final String TAG = "ComposeFragment";
    private static final String KEY_NOTE_ID = "noteIdParam";
    private static final String KEY_NOTE_COLOR = "noteColor";
    private int noteId;
    private NavigationActivity navigationActivity;

    EditText etTitle;
    EditText etContent;

    private NoteDetailsVM noteDetailsViewModel;

    public NoteDetailsFragment() {}

    public static NoteDetailsFragment newInstance(@Nullable int noteId) {
        NoteDetailsFragment fragment = new NoteDetailsFragment();

        Bundle args = new Bundle();
        args.putInt(KEY_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        if (getArguments() != null)
            this.noteId = getArguments().getInt(KEY_NOTE_ID, -1);

        initVM(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note_compose, container, false);
        etTitle = v.findViewById(R.id.et_title);
        etContent = v.findViewById(R.id.et_content);

        if (savedInstanceState == null) setEt();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationActivity = (NavigationActivity) requireActivity();
        navigationActivity.setTitle(R.string.new_note);
        navigationActivity.setUpButton(true);
    }

    private void saveNote() {
        updateCurrentNote();
        if (!noteDetailsViewModel.hasEitherField()) {
            Toast.makeText(getActivity(), getString(R.string.cannot_save_empty_note), Toast.LENGTH_SHORT).show();
            return;
        }
        noteDetailsViewModel.addOrUpdateCurrentNote();
    }

    private void changeColorTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) navigationActivity);
        builder.setTitle(R.string.choose_color)
                .setItems(Colors.colorTitles, (dialog, which) -> noteDetailsViewModel.setColor(Colors.colors[which]));
        builder.create().show();
    }

    private void initVM(Bundle savedInstanceState){
        noteDetailsViewModel = ViewModelProviders.of(this).get(NoteDetailsVM.class);
        noteDetailsViewModel.setCurrentNote(noteId, savedInstanceState != null ? savedInstanceState.getInt(KEY_NOTE_COLOR) : null);
    }

    private void setEt() {
        etTitle.setText(noteDetailsViewModel.getTitle());
        etContent.setText(noteDetailsViewModel.getContent());
    }

    private void updateCurrentNote() {
        noteDetailsViewModel.setTitle(etTitle.getText().toString());
        noteDetailsViewModel.setContent(etContent.getText().toString());
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.note_compose_menu, menu);
        noteDetailsViewModel.getColor().observe(this, integer -> menu.getItem(0).getIcon().setTint(integer));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveNote();
                return false;
            case R.id.menu_color_pick:
                changeColorTag();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        saveNote();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putInt(KEY_NOTE_COLOR, noteDetailsViewModel.getColor().getValue());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        noteDetailsViewModel.removeAllObs(this);
        super.onDestroyView();
    }
}
