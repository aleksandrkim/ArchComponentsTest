package aleksandrkim.yandextestprep.Db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by Aleksandr Kim on 10 Apr, 2018 1:59 PM for YandexTestPrep
 */

public class NotesViewModel extends AndroidViewModel {

    private MutableLiveData<String> title;
    private MutableLiveData<String> content;
    private MutableLiveData<Integer> color;

    private AppDatabase db;
    private LiveData<NoteRoom> noteRoomLiveData;
    private LiveData<List<NoteRoom>> allNotes;

    public NotesViewModel(Application application) {
        super(application);
        Log.d("ViewModel", "started");
        initFields();
    }

    private void initFields() {
        title = new MutableLiveData<>();
        title.setValue("");
        content = new MutableLiveData<>();
        content.setValue("");
        color = new MutableLiveData<>();
        color.setValue(-1); // базовый цвет - белый
        db = AppDatabase.getDb(this.getApplication());
    }

    public void subscribeToNotes(){
        allNotes = db.noteRoomDao().getAllNotesLastModifiedFirst();
    }

    public void pullNoteInfo(int id) {
        noteRoomLiveData = db.noteRoomDao().getNote(id);
        title.setValue(noteRoomLiveData.getValue().getTitle());
        content.setValue(noteRoomLiveData.getValue().getContent());
        color.setValue(noteRoomLiveData.getValue().getColor());
    }

    public boolean hasEitherField() {
        if ((title == null || title.getValue().trim().isEmpty()) && (content == null || content.getValue().trim().isEmpty()))
            return false;
        return true;
    }

    public void addNewNote() {
        if (title.getValue().trim().isEmpty()) {
            String content = this.content.getValue().concat(" ");
            title.setValue(content.substring(0, content.indexOf(' ')));
        }
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().insert(new NoteRoom(title.getValue(), content.getValue(), color.getValue()));
            }
        });
    }

    public void updateNote(final int id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().update(id, title.getValue(), content.getValue(), color.getValue());
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

    public LiveData<List<NoteRoom>> getAllNotesSortModified() {
        return allNotes;
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public void setTitle(String s) {
        title.setValue(s);
    }

    public LiveData<String> getContent() {
        return content;
    }

    public void setContent(String s) {
        content.setValue(s);
    }

    public LiveData<Integer> getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color.setValue(color);
    }
}
