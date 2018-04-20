package aleksandrkim.ArchComponentsTest;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.AsyncTask;

import java.util.Calendar;

import aleksandrkim.ArchComponentsTest.Db.AppDatabase;
import aleksandrkim.ArchComponentsTest.Db.NoteRoom;
import aleksandrkim.ArchComponentsTest.NoteCompose.Colors;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 11:37 PM for ArchComponentsTest
 */

public class NotesFeedVM extends AndroidViewModel {

    private final String TAG = "ViewModel";

    private AppDatabase db;

    private NoteRoom currentNote;
    private MutableLiveData<Integer> color;
//    private LiveData<List<NoteRoom>> allNotes;

    private LiveData<PagedList<NoteRoom>> pagedNotes;

    public NotesFeedVM(Application application) {
        super(application);
        init();
    }

    private void init() {
        db = AppDatabase.getDb(this.getApplication());
        color = new MutableLiveData<>();
        color.setValue(-1);
    }

//    public void subscribeToNotesLastModified() {
//        allNotes = db.noteRoomDao().getAllNotesLastModifiedFirst();
//    }

    public void subscribeToPagedNotes(int pageSize) {
        pagedNotes = new LivePagedListBuilder<>(db.noteRoomDao().getNotesPagedLastModifiedFirst(), pageSize).build();
    }

    public void setCurrentNote(final int index) {
//        currentNote = allNotes.getValue().get(index);
        currentNote = pagedNotes.getValue().get(index);
        color.setValue(currentNote.getColor());
    }

    public boolean hasEitherField() {
        return !(currentNote.getTitle().trim().isEmpty() && currentNote.getContent().trim().isEmpty());
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
            addNote();
        else
            updateNote();

    }

    private void addNote() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.noteRoomDao().add(currentNote);
            }
        });
    }

    public void addSampleNotes(final int count) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    NoteRoom noteRoom = new NoteRoom("sample", "content", Colors.colors[i % Colors.colors.length]);
                    db.noteRoomDao().add(noteRoom);
                }
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

//    public LiveData<List<NoteRoom>> getAllNotesSortModified() {
//        return allNotes;
//    }

    public LiveData<PagedList<NoteRoom>> getAllPagedNotes() {
        return pagedNotes;
    }

    public MutableLiveData<Integer> getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color.setValue(color);
    }
}
