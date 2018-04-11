package aleksandrkim.yandextestprep.NoteCompose;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import aleksandrkim.yandextestprep.Db.NotesViewModel;
import aleksandrkim.yandextestprep.R;

public class NoteComposeFragment extends Fragment {

    NotesViewModel notesFeedViewModel;
    String currentNoteId;

    EditText etTitle, etContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note_compose, container, false);
        etTitle = v.findViewById(R.id.tv_title);
        etContent = v.findViewById(R.id.tv_content);

        init();
        setEt();
//        bindEtToViewModel();
        bindViewModelToEt();

        return v;
    }

    private void init() {
        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.new_note));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getArguments();
        if (bundle != null)
            currentNoteId = bundle.getString(getString(R.string.current_note_id_key));

        notesFeedViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
    }

    private void setEt(){
        etTitle.setText(notesFeedViewModel.getNewTitle().getValue());
        etContent.setText(notesFeedViewModel.getNewContent().getValue());
    }

//    private void bindEtToViewModel() {
//        notesFeedViewModel.getNewTitle().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                etTitle.setText(s);
//            }
//        });
//        notesFeedViewModel.getNewContent().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                etContent.setText(s);
//            }
//        });
//    }

    private void bindViewModelToEt() {
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                notesFeedViewModel.setNewTitle(s.toString());
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
                notesFeedViewModel.setNewContent(s.toString());
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

        if (currentNoteId == null)
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
                        notesFeedViewModel.setNewColor(colors[which]);
                    }
                });
        builder.create().show();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.note_compose_menu, menu);

        notesFeedViewModel.getNewColor().observe(this, new Observer<Integer>() {
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
                getFragmentManager().popBackStack();
                return true;
            case R.id.menu_color_pick:
                changeColorTag();
                return true;
            default:
                getFragmentManager().popBackStack();
                return true;
        }
    }

    public static class ColorPickerDialogFragment extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String [] colors = new String []{"White", "Red", "Yellow", "Green", "Cyan", "Blue", "Magenta", "Dark Gray", "Black"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.pick_color)
                    .setItems(colors, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            return builder.create();
        }
    }

}
