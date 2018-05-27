package aleksandrkim.ArchComponentsTest.NoteFeed;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import aleksandrkim.ArchComponentsTest.Db.NoteRoom;
import aleksandrkim.ArchComponentsTest.NoteCompose.NoteComposeFragment;
import aleksandrkim.ArchComponentsTest.NotesFeedVM;
import aleksandrkim.ArchComponentsTest.R;

public class NotesFeedFragment extends Fragment {

    private final String TAG = "NotesFeedFragment";

    private NotesFeedVM noteFeedViewModel;
    private LinearLayoutManager adapterLayoutManager;
    private ItemTouchHelper itemTouchHelper;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private PagedFeedAdapter pagedAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteFeedViewModel = ViewModelProviders.of(this).get(NotesFeedVM.class);
        prepareRecycler();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requireActivity().setTitle(getString(R.string.note_feed));
        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        setRetainInstance(true);
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        fab = v.findViewById(R.id.fab);

        observePagedList();
        setFab();
        setRecycler();

        return v;
    }

    private void prepareRecycler() {
        // if the list was at the top, scroll upwards to show newly added items
        OnListUpdatedListener onListUpdatedListener = listSize -> {
            if (adapterLayoutManager.findFirstVisibleItemPosition() == 0 && listSize > 0) {
                adapterLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        };

        RecyclerItemClickListener itemClickListener = (position) -> {
            NoteRoom selectedNote = pagedAdapter.getNote(position);
            launchNoteComposeFragment(selectedNote.getId());
        };

        pagedAdapter = new PagedFeedAdapter(itemClickListener, onListUpdatedListener);
        itemTouchHelper = initItemTouchHelper();
    }

    private void observePagedList() {
        noteFeedViewModel.subscribeToPagedNotes(20);
        Observer<PagedList<NoteRoom>> allNotesObserver = noteRooms -> {
            pagedAdapter.submitList(noteRooms);
            Log.d(TAG, "observed: " + noteRooms.size());
        };

        if (!noteFeedViewModel.getAllPagedNotes().hasObservers()) {
            Log.d(TAG, "attached obs: ");
            noteFeedViewModel.getAllPagedNotes().observe(this, allNotesObserver);
        }
    }

    private void setFab() {
        fab.setOnClickListener(view -> launchNoteComposeFragment(-1));
    }

    private void setRecycler() {
        adapterLayoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(adapterLayoutManager);
        recyclerView.setAdapter(pagedAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void launchNoteComposeFragment(int noteId) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, NoteComposeFragment.newInstance(noteId))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    private ItemTouchHelper initItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                noteFeedViewModel.deleteNote(((NoteFeedVH) viewHolder).getId());
            }

            /** OnSwipe the foreground view is moved and the background is exposed */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null)
                    getDefaultUIUtil().onSelected(((NoteFeedVH) viewHolder).getViewForeground());
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(((NoteFeedVH) viewHolder).getViewForeground());
            }

            /** Changes the location of the deleteIcon depending on the direction of the swipe */
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView,
                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                NoteFeedVH sourceVh = (NoteFeedVH) viewHolder;
                FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) sourceVh.getDeleteIcon().getLayoutParams();
                l.gravity = dX > 0 ? (Gravity.START | Gravity.CENTER_VERTICAL) : (Gravity.END | Gravity.CENTER_VERTICAL);
                sourceVh.getDeleteIcon().setLayoutParams(l);

                getDefaultUIUtil().onDraw(c, recyclerView, sourceVh.getViewForeground(), dX, dY, actionState, isCurrentlyActive);
            }
        };

        return new ItemTouchHelper(simpleItemTouchCallback);
    }

    @Override
    public void onDestroyView() {
        noteFeedViewModel.getAllPagedNotes().removeObservers(this);
        super.onDestroyView();
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
            case R.id.menu_add_20:
                noteFeedViewModel.addSampleNotes(20);
                return true;
            default:
                return false;
        }
    }

}
