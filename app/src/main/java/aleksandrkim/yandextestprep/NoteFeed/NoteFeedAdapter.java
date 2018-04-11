package aleksandrkim.yandextestprep.NoteFeed;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import aleksandrkim.yandextestprep.Db.Note;
import aleksandrkim.yandextestprep.R;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Aleksandr Kim on 08 Apr, 2018 1:18 AM for YandexTestPrep
 */

public class NoteFeedAdapter extends RealmRecyclerViewAdapter<Note, NoteFeedAdapter.NoteFeedVH> {

    public NoteFeedAdapter(@Nullable OrderedRealmCollection<Note> data) {
        super(data, true);
    }

    @Override
    public NoteFeedVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_feed_row, parent, false);
        return new NoteFeedVH(v);
    }

    @Override
    public void onBindViewHolder(NoteFeedVH holder, int position) {
        final Note note = getItem(position);
        holder.id = note.getId();
        holder.title.setText(note.getTitle());
        if (holder.title.getText().length() == 0)
            holder.title.setVisibility(View.GONE);
        holder.content.setText(note.getContent());
        holder.date.setText(note.getDateCreatedString());
        holder.colorStrip.getBackground().setTint(note.getColor());
    }

    class NoteFeedVH extends RecyclerView.ViewHolder{
        String id;
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
