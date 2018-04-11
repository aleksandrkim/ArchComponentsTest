package aleksandrkim.yandextestprep.Db;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Aleksandr Kim on 07 Apr, 2018 11:50 PM for YandexTestPrep
 */

public class NoteDao {

    private Realm realm;

    NoteDao(Realm realm) { this.realm = realm; }

    void addNote(final Note note){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insert(note);
            }
        });
    }

    public void updateNote (final Note note){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(note);
            }
        });
    }

    public void deleteNote(final Note note) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                note.deleteFromRealm();
            }
        });
    }

    void deleteNote(final String id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Note.class).equalTo("id", id).findFirst().deleteFromRealm();
            }
        });
    }

    void deleteAllNotes() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Note.class).findAll().deleteAllFromRealm();
            }
        });
    }

    LiveRealmData<Note> getLiveAllSortedDateDesc(){
        return new LiveRealmData<>(realm.where(Note.class).sort("dateCreated", Sort.DESCENDING).findAllAsync());
    }

    RealmResults<Note> getAllSortedDateDesc(){
        return realm.where(Note.class).sort("dateCreated", Sort.DESCENDING).findAllAsync();
    }
}
