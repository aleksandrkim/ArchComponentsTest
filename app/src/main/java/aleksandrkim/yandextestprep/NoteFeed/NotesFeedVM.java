package aleksandrkim.yandextestprep.NoteFeed;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.RoomDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import aleksandrkim.yandextestprep.Db.AppDatabase;
import aleksandrkim.yandextestprep.Db.NoteRoom;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 11:37 PM for YandexTestPrep
 */

public class NotesFeedVM extends AndroidViewModel {

    private AppDatabase db;
    private LiveData<NoteRoom> noteRoomLiveData;
    private LiveData<List<NoteRoom>> allNotes;

    public NotesFeedVM(Application application) {
        super(application);
        Log.d("ViewModel", "started");
        db = AppDatabase.getDb(this.getApplication());
        allNotes = db.noteRoomDao().getAllNotesLastModifiedFirst();
    }

    public LiveData<List<NoteRoom>> getAllNotesSortModified() {
        return allNotes;
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

}
