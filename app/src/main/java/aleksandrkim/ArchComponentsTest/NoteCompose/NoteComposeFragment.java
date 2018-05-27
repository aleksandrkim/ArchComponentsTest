package aleksandrkim.ArchComponentsTest.NoteCompose;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import aleksandrkim.ArchComponentsTest.MainActivity;
import aleksandrkim.ArchComponentsTest.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoteComposeFragment extends Fragment implements MainActivity.BackEnabled {
    private static final String TAG = "ComposeFragment";
    private static final String PARAM_NOTE_ID = "noteIdParam";
    private int noteId;

    private Unbinder unbinder;

    private ComposeVM composeViewModel;

    @BindView(R.id.et_title) EditText etTitle;
    @BindView(R.id.et_content) EditText etContent;
    @BindView(R.id.color_tag) ImageView colorTag;

    public NoteComposeFragment() {}

    public static NoteComposeFragment newInstance(int noteId) {
        NoteComposeFragment fragment = new NoteComposeFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_NOTE_ID, noteId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            this.noteId = getArguments().getInt(PARAM_NOTE_ID);

        composeViewModel = ViewModelProviders.of(this).get(ComposeVM.class);

        if (savedInstanceState != null) {
            composeViewModel.setCurrentNote(noteId, true, savedInstanceState.getInt(getString(R.string.saved_color_choice)));
        } else
            composeViewModel.setCurrentNote(noteId, false, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requireActivity().setTitle(getString(R.string.new_note));
        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_note_compose, container, false);
        unbinder = ButterKnife.bind(this, v);

        if (savedInstanceState == null) setEt();

        return v;
    }

    private void setEt() {
        etTitle.setText(composeViewModel.getTitle());
        etContent.setText(composeViewModel.getContent());
    }

    private void saveNote() {
        updateVM();
        if (!composeViewModel.hasEitherField()) {
            Toast.makeText(getActivity(), getString(R.string.cannot_save_empty_note), Toast.LENGTH_SHORT).show();
            return;
        }
        composeViewModel.addOrUpdateCurrentNote();
    }

    private void updateVM() {
        composeViewModel.setTitle(etTitle.getText().toString());
        composeViewModel.setContent(etContent.getText().toString());
    }

    private void changeColorTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.pick_color)
                .setItems(Colors.colorTitles, (dialog, which) -> {
                    composeViewModel.setColor(Colors.colors[which]);
                });
        builder.create().show();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.note_compose_menu, menu);
        composeViewModel.getColor().observe(this, integer -> menu.getItem(0).getIcon().setTint(integer));
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(TAG, "onOptionsItemSelected: homeAsUp");
                saveNote();
                return false;
            case R.id.menu_save:
                saveNote();
                requireActivity().getSupportFragmentManager().popBackStack();
                return true;
            case R.id.menu_color_pick:
                changeColorTag();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        saveNote();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        outState.putInt(getString(R.string.saved_color_choice), composeViewModel.getColor().getValue());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }
}
