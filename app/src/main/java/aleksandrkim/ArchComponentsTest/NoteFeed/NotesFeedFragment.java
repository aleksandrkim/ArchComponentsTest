package aleksandrkim.ArchComponentsTest.NoteFeed;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import aleksandrkim.ArchComponentsTest.Db.NoteRoom;
import aleksandrkim.ArchComponentsTest.Db.NotesFeedVM;
import aleksandrkim.ArchComponentsTest.NoteCompose.NoteComposeFragment;
import aleksandrkim.ArchComponentsTest.R;

public class NotesFeedFragment extends Fragment {

    private final String TAG = "NotesFeedFragment";

    private NotesFeedVM noteFeedViewModel;
    private LinearLayoutManager adapterLayoutManager;
    private ItemTouchHelper itemTouchHelper;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private PagedListAdapter<NoteRoom, NoteFeedVH> pagedAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        init();
        prepareRecycler();
        observePagedList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        fab = v.findViewById(R.id.fab);

        setFab();
        bindRecycler();

        return v;
    }

    private void init() {
        setHasOptionsMenu(true);
        requireActivity().setTitle(getString(R.string.note_feed));

        noteFeedViewModel = ViewModelProviders.of(requireActivity()).get(NotesFeedVM.class);
    }

    private void prepareRecycler() {
        OnListUpdatedListener onListUpdatedListener = new OnListUpdatedListener() {
            @Override
            public void onListUpdated(int listSize) {
                if (adapterLayoutManager.findFirstVisibleItemPosition() < 1 && listSize > 0) {
                    // if the list was at the top, scroll upwards to show newly added items
                    adapterLayoutManager.scrollToPositionWithOffset(0, 0);
                }
            }
        };

        RecyclerItemClickListener itemClickListener = new RecyclerItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                noteFeedViewModel.setCurrentNote(position);
                launchNoteComposeFragment();
            }
        };

        pagedAdapter = new PagedFeedAdapter(itemClickListener, onListUpdatedListener);
        initItemTouchHelper();
    }

    private void observePagedList() {
        noteFeedViewModel.subscribeToPagedNotes(20);
        noteFeedViewModel.getAllPagedNotes().observe(this, new Observer<PagedList<NoteRoom>>() {
            @Override
            public void onChanged(@Nullable PagedList<NoteRoom> noteRooms) {
                pagedAdapter.submitList(noteRooms);
            }
        });
    }

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteFeedViewModel.resetTempNoteFields();
                launchNoteComposeFragment();
            }
        });
    }

    private void bindRecycler() {
        adapterLayoutManager = new LinearLayoutManager(requireActivity());
        recyclerView.setLayoutManager(adapterLayoutManager);
        recyclerView.setAdapter(pagedAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void launchNoteComposeFragment() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, new NoteComposeFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit();
    }

    private void initItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                noteFeedViewModel.deleteNote(((NoteFeedVH) viewHolder).getId());
            }

            /** При свайпе, двинается только передняя часть ряда и ведна иконка корзини на заднем плане
             R.layout.note_feed_row.xml
             FrameLayout - бэкграунд с иконкой для UX
             ConstraintLayout - форграунд с информацией */
            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null)
                    getDefaultUIUtil().onSelected(((NoteFeedVH) viewHolder).getViewForeground());
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(((NoteFeedVH) viewHolder).getViewForeground());
            }

            /** В зависимости от направления свайпа меняет положение иконки корзины (гравитация)*/
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

        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
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
