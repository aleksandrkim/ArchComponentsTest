package aleksandrkim.ArchComponentsTest.Db;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:41 PM for ArchComponentsTest
 */

@Dao
public abstract class NoteRoomDao {

    @Query("SELECT * FROM NoteRoom ORDER BY id DESC")
    public abstract LiveData<List<NoteRoom>> getAllNotesLastCreatedFirst();

    @Query("SELECT * FROM NoteRoom ORDER BY lastModified DESC")
    public abstract LiveData<List<NoteRoom>> getAllNotesLastModifiedFirst();

    @Query("SELECT * FROM NoteRoom ORDER BY lastModified DESC")
    public abstract DataSource.Factory<Integer, NoteRoom> getNotesPagedLastModifiedFirst();

    @Query("SELECT * FROM NoteRoom WHERE id = :id")
    public abstract NoteRoom getNoteById(int id);

    @Insert (onConflict = IGNORE)
    public abstract void add(NoteRoom noteRoom);

    @Update (onConflict = IGNORE)
    public abstract void update(NoteRoom noteRoom);

    @Transaction
    public void upsert(NoteRoom noteRoom) {
        add(noteRoom);
        update(noteRoom);
    }

    @Query("DELETE FROM NoteRoom")
    public abstract void deleteAll();

    @Query("DELETE FROM NoteRoom WHERE id = :id")
    public abstract void delete(int id);


}
