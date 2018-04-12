package aleksandrkim.yandextestprep.Db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 11:37 PM for YandexTestPrep
 */

public class NotesFeedVM extends AndroidViewModel {

    private AppDatabase db;

    private NoteRoom currentNote;
    private MutableLiveData<Integer> color;
    private LiveData<List<NoteRoom>> allNotes;

    public NotesFeedVM(Application application) {
        super(application);
        init();
        subscribeToNotesLastModified();
    }

    private void init(){
        db = AppDatabase.getDb(this.getApplication());
        color = new MutableLiveData<>();
        color.setValue(-1);
    }

    public void subscribeToNotesLastModified() {
        allNotes = db.noteRoomDao().getAllNotesLastModifiedFirst();
    }

    public void setCurrentNote(final int index) {
        currentNote = allNotes.getValue().get(index);
        color.setValue(currentNote.getColor());
    }

    public boolean hasEitherField() {
        if (currentNote.getTitle().trim().isEmpty() && currentNote.getContent().trim().isEmpty())
            return false;
        return true;
    }

    public void resetTempNoteFields() {
        currentNote = new NoteRoom("", "", -1);
        color.setValue(-1);
    }

    public void addOrUpdateCurrentNote() {
        if (currentNote.getTitle().trim().isEmpty()) {
            String content = currentNote.getContent().concat(" ");
            currentNote.setTitle(content.substring(0, content.indexOf(' ')));
        }
        currentNote.setColor(getColor().getValue());
        if (currentNote.getId() == 0)
            insertNote();
        else
            updateNote();

    }

    private void insertNote(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().insert(currentNote);
            }
        });
    }

    private void updateNote() {
        currentNote.setLastModified(Calendar.getInstance().getTime().getTime());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().update(currentNote);
            }
        });
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
        return currentNote.getTitle();
    }

    public void setTitle(String title) {
        currentNote.setTitle(title);
    }

    public String getContent() {
        return currentNote.getContent();
    }

    public void setContent(String content) {
        currentNote.setContent(content);
    }

    public LiveData<List<NoteRoom>> getAllNotesSortModified() {
        return allNotes;
    }

    public MutableLiveData<Integer> getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color.setValue(color);
//        currentNote.setColor(color);
    }
}
