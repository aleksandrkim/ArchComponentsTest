package aleksandrkim.yandextestprep.NoteFeed;

import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import aleksandrkim.yandextestprep.Db.NoteRoom;
import aleksandrkim.yandextestprep.R;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 8:29 PM for YandexTestPrep
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.NoteFeedVH> {

    private List<NoteRoom> notes;
    private RecyclerItemClickListener recyclerItemClickListener;

    public FeedAdapter (RecyclerItemClickListener recyclerItemClickListener){
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public void setNotes (final List<NoteRoom> newNotes) {
        if (this.notes == null) {
            this.notes = newNotes;
            notifyItemRangeInserted(0, newNotes.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return notes.size();
                }

                @Override
                public int getNewListSize() {
                    return newNotes.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return notes.get(oldItemPosition).getId() == newNotes.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return newNotes.get(newItemPosition).equals(notes.get(oldItemPosition));
                }
            });
            this.notes = newNotes;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public NoteFeedVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_feed_row, parent, false);
        final NoteFeedVH noteFeedVH = new NoteFeedVH(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerItemClickListener.onItemClick(view,  noteFeedVH.getAdapterPosition());
            }
        });
        return noteFeedVH;
    }

    @Override
    public void onBindViewHolder(NoteFeedVH holder, int position) {
        final NoteRoom note = notes.get(position);
        holder.id = note.getId();
        holder.title.setText(note.getTitle());
        if (holder.title.getText().length() == 0)
            holder.title.setVisibility(View.GONE);
        holder.content.setText(note.getContent());
        holder.date.setText(note.getLastModifiedString());
        holder.colorStrip.getBackground().setTint(note.getColor());
    }

    @Override
    public int getItemCount() {
        return notes == null ? 0 : notes.size();
    }

    static class NoteFeedVH extends RecyclerView.ViewHolder{
        int id;
        TextView title, content, date;
        View colorStrip;
        ConstraintLayout viewForeground;
        ImageView deleteIcon;

        NoteFeedVH(View itemView) {
            super(itemView);
            colorStrip = itemView.findViewById(R.id.color_strip);
            title = itemView.findViewById(R.id.tv_title);
            content = itemView.findViewById(R.id.tv_content);
            date = itemView.findViewById(R.id.tv_date);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            deleteIcon = itemView.findViewById(R.id.delete_icon);
        }
    }

}
