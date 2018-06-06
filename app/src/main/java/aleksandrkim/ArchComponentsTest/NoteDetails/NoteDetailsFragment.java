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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoteDetailsFragment extends Fragment implements NavigationActivity.BackEnabled {
    public static final String TAG = "ComposeFragment";
    private static final String KEY_NOTE_ID = "noteIdParam";
    private static final String KEY_NOTE_COLOR = "noteColor";
    private int noteId;
    private NavigationActivity navigationActivity;

    private Unbinder unbinder;
    @BindView(R.id.et_title) EditText etTitle;
    @BindView(R.id.et_content) EditText etContent;

    private NoteDetailsVM composeViewModel;

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
        unbinder = ButterKnife.bind(this, v);

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
        if (!composeViewModel.hasEitherField()) {
            Toast.makeText(getActivity(), getString(R.string.cannot_save_empty_note), Toast.LENGTH_SHORT).show();
            return;
        }
        composeViewModel.addOrUpdateCurrentNote();
    }

    private void changeColorTag() {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) navigationActivity);
        builder.setTitle(R.string.choose_color)
                .setItems(Colors.colorTitles, (dialog, which) -> composeViewModel.setColor(Colors.colors[which]));
        builder.create().show();
    }

    private void initVM(Bundle savedInstanceState){
        composeViewModel = ViewModelProviders.of(this).get(NoteDetailsVM.class);
        composeViewModel.setCurrentNote(noteId, savedInstanceState != null ? savedInstanceState.getInt(KEY_NOTE_COLOR) : null);
    }

    private void setEt() {
        etTitle.setText(composeViewModel.getTitle());
        etContent.setText(composeViewModel.getContent());
    }

    private void updateCurrentNote() {
        composeViewModel.setTitle(etTitle.getText().toString());
        composeViewModel.setContent(etContent.getText().toString());
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
        outState.putInt(KEY_NOTE_COLOR, composeViewModel.getColor().getValue());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null) unbinder.unbind();
        super.onDestroyView();
    }
}
