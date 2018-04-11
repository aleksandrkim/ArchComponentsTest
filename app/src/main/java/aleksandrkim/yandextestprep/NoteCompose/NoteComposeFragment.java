package aleksandrkim.yandextestprep.NoteCompose;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import aleksandrkim.yandextestprep.Db.NotesFeedVM;
import aleksandrkim.yandextestprep.R;

public class NoteComposeFragment extends Fragment {

    String TAG = "NoteComposeFragment";
    private NotesFeedVM notesFeedViewModel;
    private int currentNoteId = -1;

    private EditText etTitle, etContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View v = inflater.inflate(R.layout.fragment_note_compose, container, false);
        etTitle = v.findViewById(R.id.tv_title);
        etContent = v.findViewById(R.id.tv_content);

        init();
        setEt();
        bindViewModelToEt();

        return v;
    }

    private void init() {
        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.new_note));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        notesFeedViewModel = ViewModelProviders.of(getActivity()).get(NotesFeedVM.class);
    }

    private void setEt(){
        etTitle.setText(notesFeedViewModel.getTitle().getValue());
        etContent.setText(notesFeedViewModel.getContent().getValue());
    }

    private void bindViewModelToEt() {
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                notesFeedViewModel.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                notesFeedViewModel.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void saveNote() {
        if (!notesFeedViewModel.hasEitherField()) {
            Toast.makeText(getActivity(), getString(R.string.cannot_save_empty_note), Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentNoteId == -1)
            notesFeedViewModel.addNewNote();
        else
            notesFeedViewModel.updateNote(currentNoteId);
    }

    private void changeColorTag() {
        final String [] colorTitles = new String []{"White", "Red", "Magenta", "Yellow", "Green",
                "Cyan", "Blue", "Dark Gray"};
        final int [] colors = new int []{Color.WHITE, Color.RED, Color.MAGENTA, Color.YELLOW, Color.GREEN,
                Color.CYAN, Color.BLUE, Color.DKGRAY};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_color)
                .setItems(colorTitles, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        notesFeedViewModel.setColor(colors[which]);
                    }
                });
        builder.create().show();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.note_compose_menu, menu);

        notesFeedViewModel.getColor().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                menu.getItem(0).getIcon().setTint(integer);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveNote();
                Log.i("onOptions", " " + getActivity().getSupportFragmentManager().getBackStackEntryCount());
                getActivity().getSupportFragmentManager().popBackStack();
                Log.i("onOptions", " " + getActivity().getSupportFragmentManager().getBackStackEntryCount());
                return true;
            case R.id.menu_color_pick:
                changeColorTag();
                return true;
            default:
                Log.i("onOptions", " " + getActivity().getSupportFragmentManager().getBackStackEntryCount());
                getActivity().getSupportFragmentManager().popBackStack();
                Log.i("onOptions", " " + getActivity().getSupportFragmentManager().getBackStackEntryCount());
                return true;
        }
    }
}
