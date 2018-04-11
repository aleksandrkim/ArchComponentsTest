package aleksandrkim.yandextestprep.NoteFeed;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import aleksandrkim.yandextestprep.Db.NotesViewModel;
import aleksandrkim.yandextestprep.NoteCompose.NoteComposeFragment;
import aleksandrkim.yandextestprep.R;

public class NoteFeedFragment extends Fragment {

    private NotesViewModel noteFeedViewModel;
    private RecyclerView recyclerView;
    private NoteFeedAdapter noteFeedAdapter;

    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        fab = v.findViewById(R.id.fab);

        init();

        setFab();

        setRecyclerView();
        return v;
    }

    private void init() {
        setHasOptionsMenu(true);
        getActivity().setTitle(getString(R.string.note_feed));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        noteFeedViewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNoteComposeFragment();
            }
        });
    }

    private void launchNoteComposeFragment() {
        NoteComposeFragment noteComposeFragment = new NoteComposeFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, noteComposeFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(noteComposeFragment.getClass().getSimpleName())
                .commit();
    }

    private void setRecyclerView() {
        initRecyclerView();
        setRecyclerViewSwipe();

//        notes.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
//            @Override
//            public void onChange(RealmResults<Note> notes) {
//                recyclerViewLayoutManager.scrollToPositionWithOffset(0, 0);
//            }
//        });
    }

    private void initRecyclerView() {
        noteFeedAdapter = new NoteFeedAdapter(noteFeedViewModel.getAllNotes());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(noteFeedAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    private void setRecyclerViewSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                noteFeedViewModel.deleteNote(((NoteFeedAdapter.NoteFeedVH) viewHolder).id);
            }

            /** При свайпе, двинается только передняя часть ряда и ведна иконка корзини на заднем плане
             R.layout.note_feed_row.xml
             FrameLayout - бэкграунд с иконкой для UX
             ConstraintLayout - форграунд с информацией */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null)
                    getDefaultUIUtil().onSelected(((NoteFeedAdapter.NoteFeedVH) viewHolder).viewForeground);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(((NoteFeedAdapter.NoteFeedVH) viewHolder).viewForeground);
            }

            /** В зависимости от направления свайпа меняет положение иконки корзины (гравитация)*/
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {

                NoteFeedAdapter.NoteFeedVH sourceVh = (NoteFeedAdapter.NoteFeedVH) viewHolder;
                FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) sourceVh.deleteIcon.getLayoutParams();
                l.gravity = dX > 0 ? Gravity.START | Gravity.CENTER_VERTICAL : Gravity.END | Gravity.CENTER_VERTICAL;
                sourceVh.deleteIcon.setLayoutParams(l);

                getDefaultUIUtil().onDraw(c, recyclerView, sourceVh.viewForeground, dX, dY,
                        actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.note_feed_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_erase_all:
                noteFeedViewModel.deleteAllNotes();
                return true;
            default:
                return false;
        }
    }

}
