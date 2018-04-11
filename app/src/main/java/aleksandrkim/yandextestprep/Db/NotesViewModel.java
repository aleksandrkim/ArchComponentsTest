package aleksandrkim.yandextestprep.Db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Aleksandr Kim on 10 Apr, 2018 1:59 PM for YandexTestPrep
 */

public class NotesViewModel extends AndroidViewModel {

    private Realm realm;
    private NoteDao noteDao;
    private MutableLiveData<String> title;
    private MutableLiveData<String> content;
    private MutableLiveData<Integer> color;

    private AppDatabase db;
    private MutableLiveData<NoteRoom> noteRoomLiveData;
//    private LiveRealmData allNotes;

    public NotesViewModel(Application application) {
        super(application);

        realm = Realm.getDefaultInstance();
        noteDao = new NoteDao(realm);
//        if (title == null) {
            title = new MutableLiveData<>();
            title.setValue("");
//        }
//        if (content == null) {
            content = new MutableLiveData<>();
            content.setValue("");
//        }
//        if (color == null) {
            color = new MutableLiveData<>();
            color.setValue(-1);
//        }
        db = AppDatabase.getDb(this.getApplication());
    }

    public void getCurrentNote (int id) {
        noteRoomLiveData = db.noteRoomDao().getMutableNote(id);
        title.setValue(noteRoomLiveData.getValue().getTitle());
        content.setValue(noteRoomLiveData.getValue().getContent());
        color.setValue(noteRoomLiveData.getValue().getColor());
    }

    public boolean hasEitherField() {
        if ((title == null || title.getValue().trim().isEmpty()) && (content == null || content.getValue().trim().isEmpty()))
            return false;
        return true;
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

    //    private void subscribeToAllNotes(){
//        allNotes = noteDao.getLiveAllSortedDateDesc();
//    }

    public void addNewNote() {
        if (title.getValue().trim().isEmpty()){
            String content = this.content.getValue().concat(" ");
            title.setValue(content.substring(0, content.indexOf(' ')));
        }
        db.noteRoomDao().insert(new NoteRoom(title.getValue(), content.getValue(), color.getValue()));
//        noteDao.addNote(new Note(title.getValue(), content.getValue(), color.getValue()));
    }

    public void updateNote(int id) {
//        Note note = realm.where(Note.class).equalTo("id", id).findFirst();
//        if (note == null) {
//            addNewNote();
//        } else {
//            note.setTitle(title.getValue());
//            note.setContent(content.getValue());
//            note.setColor(color.getValue());
//            noteDao.updateNote(note);
//        }
        db.noteRoomDao().update(id, title.getValue(), content.getValue(), color.getValue());
    }

    public void deleteNote(int id) {
        db.noteRoomDao().delete(id);
    }

    public void deleteAllNotes() {
//        noteDao.deleteAllNotes();
        db.noteRoomDao().deleteAll();
    }

//    public LiveRealmData getLiveAllNotes () {
//        return allNotes;
//    }

    public List<LiveData<NoteRoom>> getAllNotes() {
        return db.noteRoomDao().getAllNotesLastModifiedFirst();
//        return noteDao.getAllSortedDateDesc();
    }

    @Override
    protected void onCleared() {
        realm.close();
        super.onCleared();
    }

}
