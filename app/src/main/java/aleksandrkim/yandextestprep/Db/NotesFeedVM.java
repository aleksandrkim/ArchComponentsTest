package aleksandrkim.yandextestprep.Db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 11:37 PM for YandexTestPrep
 */

public class NotesFeedVM extends AndroidViewModel{

    private AppDatabase db;
    private String title, content;
    private MutableLiveData<Integer> color;

    private NoteRoom currentNote;
    private LiveData<List<NoteRoom>> allNotes;

    public NotesFeedVM(Application application) {
        super(application);
        initFields();
        subscribeToNotes();
    }

    public void subscribeToNotes() {
        allNotes = db.noteRoomDao().getAllNotesLastModifiedFirst();
    }

    private void initFields() {
        color = new MutableLiveData<>();
        color.setValue(-1);
        db = AppDatabase.getDb(this.getApplication());
    }

    public void setCurrentNote(final int index) {
        currentNote = allNotes.getValue().get(index);
        title = (currentNote.getTitle());
        content = (currentNote.getContent());
        color.setValue(currentNote.getColor());
    }

    public boolean hasEitherField() {
        if ((title == null || title.trim().isEmpty()) && (content == null || content.trim().isEmpty()))
            return false;
        return true;
    }

    public void addNewNote() {
        if (title.trim().isEmpty()) {
            String content = this.content.concat(" ");
            title = (content.substring(0, content.indexOf(' ')));
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().insert(new NoteRoom(title, content, color.getValue()));
            }
        });
    }

    public void updateNote(final int id) {
        currentNote.setTitle(title);
        currentNote.setContent(content);
        currentNote.setColor(color.getValue());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().update(currentNote);
            }
        });
    }

    public void resetTempNoteFields() {
        currentNote = new NoteRoom("","", -1);
        title = content = "";
        color.setValue(-1); // базовый цвет - белый
    }

    public NoteRoom getCurrentNote() {
        return currentNote;
    }

    public void deleteNote(final int id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().delete(id);
            }
        });
    }

    public void deleteAllNotes() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().deleteAll();
            }
        });
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

    public LiveData<List<NoteRoom>> getAllNotesSortModified() {
        return allNotes;
    }

    public MutableLiveData<Integer> getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color.setValue(color);
    }
}
