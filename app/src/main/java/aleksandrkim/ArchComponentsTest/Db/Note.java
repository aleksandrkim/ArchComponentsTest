package aleksandrkim.ArchComponentsTest.Db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.v7.util.DiffUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:27 PM for ArchComponentsTest
 */

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private int color;
    private long lastModified;
    private long createdTime;

    @Ignore
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM", new Locale("Ru"));

    public Note(String title, String content, int color) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.lastModified = Calendar.getInstance().getTimeInMillis();
        this.createdTime = Calendar.getInstance().getTimeInMillis();
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
        return simpleDateFormat.format(lastModified);
    }

    public String getCreatedTimeString() {
        return simpleDateFormat.format(createdTime);
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public static final DiffUtil.ItemCallback<Note> DIFF_ITEM_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(Note oldItem, Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Note oldItem, Note newItem) {
            return oldItem.equals(newItem);
        }
    };

    private boolean equals(Note obj) {
        return this.id == obj.id && this.title.equals(obj.getTitle()) && this.content.equals(obj.getContent())
                && this.color == obj.getColor() && this.lastModified == obj.getLastModified();
    }
}
