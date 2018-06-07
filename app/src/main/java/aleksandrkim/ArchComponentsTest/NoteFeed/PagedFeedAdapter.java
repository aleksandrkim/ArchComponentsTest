package aleksandrkim.ArchComponentsTest.NoteFeed;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aleksandrkim.ArchComponentsTest.Db.Note;
import aleksandrkim.ArchComponentsTest.R;

/**
 * Created by Aleksandr Kim on 20 Apr, 2018 4:25 PM for ArchComponentsTest
 */

public class PagedFeedAdapter extends PagedListAdapter<Note, NoteFeedVH> {

    private RecyclerItemClickListener recyclerItemClickListener;
    private OnListUpdatedListener onListUpdatedListener;

    PagedFeedAdapter(RecyclerItemClickListener recyclerItemClickListener, OnListUpdatedListener onListUpdatedListener) {
        super(Note.Companion.getDIFF_ITEM_CALLBACK());
        this.recyclerItemClickListener = recyclerItemClickListener;
        this.onListUpdatedListener = onListUpdatedListener;
    }

    @NonNull
    @Override
    public NoteFeedVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_feed_row, parent, false);
        final NoteFeedVH noteFeedVH = new NoteFeedVH(v);
        v.setOnClickListener((view) -> recyclerItemClickListener.onItemClick(noteFeedVH.getAdapterPosition()));
        return noteFeedVH;
    }

    @Override
    public void onCurrentListChanged(@Nullable PagedList<Note> currentList) {
        super.onCurrentListChanged(currentList);
        onListUpdatedListener.onListUpdated(currentList.size());
    }

    public Note getNote(int position) {
        return getItem(position);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteFeedVH holder, int position) {
        Note note = getItem(position);
        if (note != null) {
            holder.bind(note);
        } else {
            holder.clear();
        }
    }
}
