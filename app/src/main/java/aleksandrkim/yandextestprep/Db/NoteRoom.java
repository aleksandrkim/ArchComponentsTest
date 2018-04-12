package aleksandrkim.yandextestprep.Db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:27 PM for YandexTestPrep
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

    public boolean equals(NoteRoom obj) {
        return this.id == obj.id && this.title.equals(obj.getTitle()) && this.content.equals(obj.getContent())
                && this.color == obj.getColor() && this.lastModified == obj.getLastModified();
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
}
