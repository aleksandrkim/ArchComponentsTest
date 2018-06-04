package aleksandrkim.ArchComponentsTest.Db;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:41 PM for ArchComponentsTest
 */

@Dao
public abstract class NoteDao {

    @Query("SELECT * FROM Note ORDER BY lastModified DESC")
    public abstract DataSource.Factory<Integer, Note> getNotesPagedLastModifiedFirst();

    @Query("SELECT * FROM Note ORDER BY createdTime DESC")
    public abstract DataSource.Factory<Integer, Note> getNotesPagedLastCreatedFirst();

    @Query("SELECT * FROM Note WHERE id = :id")
    public abstract Note getNoteById(int id);

    @Insert (onConflict = IGNORE)
    public abstract void add(Note note);

    @Update (onConflict = IGNORE)
    public abstract void update(Note note);

    @Transaction
    public void upsert(Note note) {
        add(note);
        update(note);
    }

    @Query("DELETE FROM Note")
    public abstract void deleteAll();

    @Query("DELETE FROM Note WHERE id = :id")
    public abstract void delete(int id);


}
