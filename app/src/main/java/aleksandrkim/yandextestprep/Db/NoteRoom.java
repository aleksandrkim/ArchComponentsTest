package aleksandrkim.yandextestprep.Db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

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
    private Date lastModified;

    public NoteRoom(String title, String content, int color) {
        this.title = title;
        this.content = content;
        this.color = color;
        this.lastModified = Calendar.getInstance().getTime();
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

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
}
