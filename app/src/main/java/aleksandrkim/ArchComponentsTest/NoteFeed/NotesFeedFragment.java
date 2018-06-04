package aleksandrkim.ArchComponentsTest.NoteFeed;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import aleksandrkim.ArchComponentsTest.HostActivity.NavigationActivity;
import aleksandrkim.ArchComponentsTest.HostActivity.NotesFeedVM;
import aleksandrkim.ArchComponentsTest.NoteCompose.NoteComposeFragment;
import aleksandrkim.ArchComponentsTest.R;
import aleksandrkim.ArchComponentsTest.Utils.SwipeCallback;

public class NotesFeedFragment extends Fragment {
    public static final String TAG = "NotesFeedFragment";
    NavigationActivity navigationActivity;

    private NotesFeedVM noteFeedViewModel;
    private LinearLayoutManager adapterLayoutManager;
    private ItemTouchHelper itemTouchHelper;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private PagedFeedAdapter pagedAdapter;

    public static NotesFeedFragment newInstance() {
        NotesFeedFragment fragment = new NotesFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteFeedViewModel = ViewModelProviders.of(this).get(NotesFeedVM.class);
        prepareRecycler();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        fab = v.findViewById(R.id.fab);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navigationActivity = (NavigationActivity) requireActivity();
        navigationActivity.setTitle(R.string.note_feed);
        navigationActivity.setUpButton(false);
        setFab();
        setRecycler();
        observePagedList();
    }

    private void prepareRecycler() {
        // if the list was at the top, scroll upwards to show newly added items
        OnListUpdatedListener onListUpdatedListener = listSize -> {
            if (adapterLayoutManager.findFirstVisibleItemPosition() == 0 && listSize > 0) {
                adapterLayoutManager.scrollToPositionWithOffset(0, 0);
            }
        };
        RecyclerItemClickListener itemClickListener = (pos) -> launchNoteComposeFragment(pagedAdapter.getNote(pos).getId());
        pagedAdapter = new PagedFeedAdapter(itemClickListener, onListUpdatedListener);

        SwipeCallback swipeCallback = new SwipeCallback(
                (viewHolder, holder) -> noteFeedViewModel.deleteNote(((NoteFeedVH) viewHolder).getId()));
        itemTouchHelper = new ItemTouchHelper(swipeCallback);
    }

    private void observePagedList() {
        noteFeedViewModel.subscribeToPagedNotes(20);
        noteFeedViewModel.getAllPagedNotes().observe(this, noteRooms -> pagedAdapter.submitList(noteRooms));
    }

    private void setFab() {
        fab.setOnClickListener(view -> launchNoteComposeFragment(-1));
    }

    private void setRecycler() {
        adapterLayoutManager = new LinearLayoutManager((Context) navigationActivity);
        recyclerView.setLayoutManager(adapterLayoutManager);
        recyclerView.setAdapter(pagedAdapter);
        recyclerView.setHasFixedSize(true);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void launchNoteComposeFragment(int noteId) {
        navigationActivity.launchWholeFragment(NoteComposeFragment.newInstance(noteId), NoteComposeFragment.TAG);
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
                noteFeedViewModel.addSampleNotes(5);
                return true;
            default:
                return false;
        }
    }

}
