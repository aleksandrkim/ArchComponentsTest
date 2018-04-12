package aleksandrkim.ArchComponentsTest.NoteCompose;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import aleksandrkim.ArchComponentsTest.Db.NotesFeedVM;
import aleksandrkim.ArchComponentsTest.R;

public class NoteComposeFragment extends Fragment {

    private NotesFeedVM notesFeedViewModel;

    private EditText etTitle, etContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View v = inflater.inflate(R.layout.fragment_note_compose, container, false);
        etTitle = v.findViewById(R.id.tv_title);
        etContent = v.findViewById(R.id.tv_content);

        init(savedInstanceState);

        return v;
    }

    private void init(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.new_note));

        notesFeedViewModel = ViewModelProviders.of(getActivity()).get(NotesFeedVM.class);
        if (savedInstanceState != null)
            notesFeedViewModel.setColor(savedInstanceState.getInt(getString(R.string.saved_color_choice)));
        else
            setEt();
    }

    private void setEt() {
        etTitle.setText(notesFeedViewModel.getTitle());
        etContent.setText(notesFeedViewModel.getContent());
    }

    private void saveNote() {
        updateVM();
        if (!notesFeedViewModel.hasEitherField()) {
            Toast.makeText(getActivity(), getString(R.string.cannot_save_empty_note), Toast.LENGTH_SHORT).show();
            return;
        }
        notesFeedViewModel.addOrUpdateCurrentNote();
    }

    private void updateVM() {
        notesFeedViewModel.setTitle(etTitle.getText().toString());
        notesFeedViewModel.setContent(etContent.getText().toString());
    }

    private void changeColorTag() {
        final String[] colorTitles = new String[]{"White", "Red", "Magenta", "Yellow", "Green",
                "Cyan", "Blue", "Dark Gray"};
        final int[] colors = new int[]{Color.WHITE, Color.RED, Color.MAGENTA, Color.YELLOW, Color.GREEN,
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
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            case R.id.menu_color_pick:
                changeColorTag();
                return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(getString(R.string.saved_color_choice), notesFeedViewModel.getColor().getValue());
        super.onSaveInstanceState(outState);
    }
}
