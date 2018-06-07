package aleksandrkim.ArchComponentsTest.NoteFeed;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import aleksandrkim.ArchComponentsTest.HostActivity.NavigationActivity;
import aleksandrkim.ArchComponentsTest.NoteDetails.NoteDetailsFragment;
import aleksandrkim.ArchComponentsTest.R;
import aleksandrkim.ArchComponentsTest.Utils.SwipeCallback;

public class NotesFeedFragment extends Fragment implements NavigationActivity.BackEnabled {
    public static final String TAG = "NotesFeedFragment";
    NavigationActivity navigationActivity;

    private NotesFeedVM noteFeedViewModel;
    private LinearLayoutManager adapterLayoutManager;
    private ItemTouchHelper itemTouchHelper;

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private PagedFeedAdapter pagedAdapter;
    private boolean scrollToTop = false;

    public static NotesFeedFragment newInstance() {
        NotesFeedFragment fragment = new NotesFeedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
        setHasOptionsMenu(true);
        initVM();
        prepareRecycler();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        coordinatorLayout = v.findViewById(R.id.coordinator);
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
        observeSwipeEvent();
    }

    private void prepareRecycler() {
        pagedAdapter = new PagedFeedAdapter(
                pos -> launchNoteComposeFragment(pagedAdapter.getNote(pos).getId()),
                listSize -> {
                    if (scrollToTop) {
                        adapterLayoutManager.scrollToPositionWithOffset(0, 0);
                        scrollToTop = false;
                    }
                });

        SwipeCallback swipeCallback = new SwipeCallback(
                (viewHolder, holder) -> noteFeedViewModel.swipe(((NoteFeedVH) viewHolder).getId(), viewHolder.getLayoutPosition()));

        itemTouchHelper = new ItemTouchHelper(swipeCallback);
    }

    private void observePagedList() {
        noteFeedViewModel.getAllPagedNotes().observe(this, noteRooms -> {
            if (pagedAdapter.getCurrentList() != null && noteRooms != null &&
                    pagedAdapter.getCurrentList().size() < noteRooms.size() &&
                    adapterLayoutManager.findFirstVisibleItemPosition() == 0) {
                scrollToTop = true;
            }

            pagedAdapter.submitList(noteRooms);
        });
    }

    private void observeSwipeEvent() {
        noteFeedViewModel.getSwipedNote().observe(this,
                integerEvent -> {
                    if (integerEvent.getContentIfAvailable() != null) {
                        showSnackbar(R.string.note_deleted, Snackbar.LENGTH_SHORT, R.string.undo,
                                view -> pagedAdapter.notifyItemChanged(integerEvent.peekContent().second),
                                () -> noteFeedViewModel.deleteNote(integerEvent.peekContent().first));
                    }
                }
        );
    }

    private void showSnackbar(int displayTextId, int duration, int actionTextId,
                              View.OnClickListener onActionClicked, Runnable onDismissed) {
        Snackbar.make(coordinatorLayout, displayTextId, duration)
                .setAction(actionTextId, onActionClicked)
                .addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) onDismissed.run();
                        super.onDismissed(transientBottomBar, event);
                    }
                }).show();
    }

    private void initVM() {
        noteFeedViewModel = ViewModelProviders.of(this).get(NotesFeedVM.class);
        noteFeedViewModel.subscribeToPagedNotes(20);
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
        navigationActivity.launchWholeFragment(NoteDetailsFragment.newInstance(noteId), NoteDetailsFragment.TAG);
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

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed() called");
        navigationActivity.finish();
    }

    @Override
    public void onDestroyView() {
        noteFeedViewModel.removeAllObs(this);
        super.onDestroyView();
    }

}
