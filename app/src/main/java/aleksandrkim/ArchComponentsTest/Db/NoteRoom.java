package aleksandrkim.ArchComponentsTest.Db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.v7.util.DiffUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:27 PM for ArchComponentsTest
 */

@Entity
public class NoteRoom {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private int color;
    private long lastModified;

    public NoteRoom(String title, String content, int color) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.lastModified = Calendar.getInstance().getTimeInMillis();
    }

    public void setTitleIfEmpty () {
        if (getTitle().trim().isEmpty()) {
            String content = getContent().concat(" ");
            setTitle(content.substring(0, content.indexOf(' ')));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getLastModifiedString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM", new Locale("Ru"));
        return simpleDateFormat.format(lastModified);
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public static final DiffUtil.ItemCallback<NoteRoom> DIFF_ITEM_CALLBACK = new DiffUtil.ItemCallback<NoteRoom>() {
        @Override
        public boolean areItemsTheSame(NoteRoom oldItem, NoteRoom newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(NoteRoom oldItem, NoteRoom newItem) {
            return oldItem.equals(newItem);
        }
    };

    private boolean equals(NoteRoom obj) {
        return this.id == obj.id && this.title.equals(obj.getTitle()) && this.content.equals(obj.getContent())
                && this.color == obj.getColor() && this.lastModified == obj.getLastModified();
    }
}
