package aleksandrkim.ArchComponentsTest.NoteCompose;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.Calendar;

import aleksandrkim.ArchComponentsTest.Db.AppDatabase;
import aleksandrkim.ArchComponentsTest.Db.Note;

/**
 * Created by Aleksandr Kim on 27 May, 2018 11:21 PM for ArchComponentsTest
 */

public class ComposeVM extends AndroidViewModel {

    private static final String TAG = "ComposeVM";

    private AppDatabase db;

    private Note currentNote;
    private MutableLiveData<Integer> color;

    public ComposeVM(Application application) {
        super(application);
        db = AppDatabase.getDb(this.getApplication());
        color = new MutableLiveData<>();
    }

    void setCurrentNote(final int id, @Nullable Integer savedColor) {
        AsyncTask.execute(() -> {
            currentNote = id == -1
                    ? new Note("", "", -1)
                    : db.noteRoomDao().getNoteById(id);
            color.postValue(savedColor == null
                    ? currentNote.getColor()
                    : savedColor);
        });
    }

    boolean hasEitherField() {
        return !(currentNote.getTitle().trim().isEmpty() && currentNote.getContent().trim().isEmpty());
    }

    public void addOrUpdateCurrentNote() {
        currentNote.setTitleIfEmpty();
        currentNote.setColor(color.getValue());
        currentNote.setLastModified(Calendar.getInstance().getTime().getTime());
        AsyncTask.execute(() -> db.noteRoomDao().upsert(currentNote));
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

    public MutableLiveData<Integer> getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color.setValue(color);
    }
}