package aleksandrkim.yandextestprep.Db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 11:37 PM for YandexTestPrep
 */

public class NotesFeedVM extends AndroidViewModel {

    private AppDatabase db;
    private MutableLiveData<String> title;
    private MutableLiveData<String> content;
    private MutableLiveData<Integer> color;

    private NoteRoom currentNote;
    private LiveData<List<NoteRoom>> allNotes;

    public NotesFeedVM(Application application) {
        super(application);
        initFields();
        subscribeToNotes();
    }

    public void subscribeToNotes(){
        Log.d("ViewModel", "subscribed");
        allNotes = db.noteRoomDao().getAllNotesLastModifiedFirst();
    }

    private void initFields() {
        title = new MutableLiveData<>();
        content = new MutableLiveData<>();
        color = new MutableLiveData<>();
        resetTempNoteFields();
        db = AppDatabase.getDb(this.getApplication());
    }

    public void setCurrentNote(int index) {
        currentNote = allNotes.getValue().get(index);
        title.setValue(currentNote.getTitle());
        content.setValue(currentNote.getContent());
        color.setValue(currentNote.getColor());
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
                resetTempNoteFields();
            }
        });
    }

    public void updateNote(final int id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().update(id, title.getValue(), content.getValue(), color.getValue());
                resetTempNoteFields();
            }
        });
    }

    public void resetTempNoteFields(){
        currentNote = null;
        title.setValue("");
        content.setValue("");
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

    public LiveData<List<NoteRoom>> getAllNotesSortModified() {
        return allNotes;
    }

}
