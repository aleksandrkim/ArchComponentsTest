package aleksandrkim.yandextestprep.Db;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Aleksandr Kim on 07 Apr, 2018 11:27 PM for YandexTestPrep
 */

public class Note extends RealmObject {

    @PrimaryKey
    @Required
    private String id;

    private String title;
    private String content;
    private int color;
    private Date dateCreated;

    public Note() {
        this.id = UUID.randomUUID().toString();
        this.dateCreated = Calendar.getInstance().getTime();
    }

    public Note(String title, String content, int color) {
        this.id = UUID.randomUUID().toString();
        this.dateCreated = Calendar.getInstance().getTime();
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public Note(Note other) {
        this.id = UUID.randomUUID().toString();
        this.dateCreated = Calendar.getInstance().getTime();
        this.title = other.title;
        this.content = other.content;
        this.color = other.color;
    }

    public String getId() {
        return id;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getDateCreatedString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM", new Locale("Ru"));
        return simpleDateFormat.format(dateCreated);
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
