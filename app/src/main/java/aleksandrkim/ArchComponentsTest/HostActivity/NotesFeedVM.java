package aleksandrkim.ArchComponentsTest.HostActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.AsyncTask;

import aleksandrkim.ArchComponentsTest.Db.AppDatabase;
import aleksandrkim.ArchComponentsTest.Db.NoteRoom;
import aleksandrkim.ArchComponentsTest.Utils.Colors;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 11:37 PM for ArchComponentsTest
 */

public class NotesFeedVM extends AndroidViewModel {

    private final String TAG = "NotesFeedVM";

    private AppDatabase db;

    private LiveData<PagedList<NoteRoom>> pagedNotes;

    public NotesFeedVM(Application application) {
        super(application);
        db = AppDatabase.getDb(this.getApplication());
    }

    public void subscribeToPagedNotes(int pageSize) {
        pagedNotes = new LivePagedListBuilder<>(db.noteRoomDao().getNotesPagedLastModifiedFirst(), pageSize).build();
    }

    public void addSampleNotes(final int count) {
        AsyncTask.execute(() -> {
            for (int i = 0; i < count; i++) {
                NoteRoom noteRoom = new NoteRoom("sample", "content", Colors.colors[i % Colors.colors.length]);
                db.noteRoomDao().add(noteRoom);
            }
        });
    }

    public void deleteNote(final int id) {
        AsyncTask.execute(() -> db.noteRoomDao().delete(id));
    }

    public void deleteAllNotes() {
        AsyncTask.execute(() -> db.noteRoomDao().deleteAll());
    }

    public LiveData<PagedList<NoteRoom>> getAllPagedNotes() {
        return pagedNotes;
    }
}
