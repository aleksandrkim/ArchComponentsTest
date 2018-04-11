package aleksandrkim.yandextestprep.Db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Aleksandr Kim on 10 Apr, 2018 1:59 PM for YandexTestPrep
 */

public class NotesViewModel extends ViewModel {

    private Realm realm;
    private NoteDao noteDao;
    private MutableLiveData<String> newTitle;
    private MutableLiveData<String> newContent;
    private MutableLiveData<Integer> newColor;
//    private LiveRealmData allNotes;

    public NotesViewModel() {
        realm = Realm.getDefaultInstance();
        noteDao = new NoteDao(realm);
        if (newTitle == null) {
            newTitle = new MutableLiveData<>();
            newTitle.setValue("");
        }
        if (newContent == null) {
            newContent = new MutableLiveData<>();
            newContent.setValue("");
        }
        if (newColor == null) {
            newColor = new MutableLiveData<>();
            newColor.setValue(-1);
        }
    }

    public boolean hasEitherField() {
        if ((newTitle == null || newTitle.getValue().trim().isEmpty()) && (newContent == null || newContent.getValue().trim().isEmpty()))
            return false;
        return true;
    }

    public LiveData<String> getNewTitle() {
        return newTitle;
    }

    public void setNewTitle(String s) {
        newTitle.setValue(s);
    }

    public LiveData<String> getNewContent() {
        return newContent;
    }

    public void setNewContent(String s) {
        newContent.setValue(s);
    }

    public LiveData<Integer> getNewColor() {
        return newColor;
    }

    public void setNewColor(int color) {
        this.newColor.setValue(color);
    }

    //    private void subscribeToAllNotes(){
//        allNotes = noteDao.getLiveAllSortedDateDesc();
//    }

    public void addNewNote() {
        if (newTitle.getValue().trim().isEmpty()){
            String content = newContent.getValue().concat(" ");
            newTitle.setValue(content.substring(0, content.indexOf(' ')));
        }
        noteDao.addNote(new Note(newTitle.getValue(), newContent.getValue(), newColor.getValue()));
    }

    public void updateNote(String id) {
        Note note = realm.where(Note.class).equalTo("id", id).findFirst();
        if (note == null) {
            addNewNote();
        } else {
            note.setTitle(newTitle.getValue());
            note.setContent(newContent.getValue());
            note.setColor(newColor.getValue());
            noteDao.updateNote(note);
        }
    }

    public void deleteNote(String id) {
        noteDao.deleteNote(id);
    }

    public void deleteAllNotes() {
        noteDao.deleteAllNotes();
    }

//    public LiveRealmData getLiveAllNotes () {
//        return allNotes;
//    }

    public void showToast(){

    }

    public RealmResults<Note> getAllNotes() {
        return noteDao.getAllSortedDateDesc();
    }

    @Override
    protected void onCleared() {
        realm.close();
        super.onCleared();
    }

}
