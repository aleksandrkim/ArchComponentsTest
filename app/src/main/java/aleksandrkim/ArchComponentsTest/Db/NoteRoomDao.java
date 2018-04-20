package aleksandrkim.ArchComponentsTest.Db;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:41 PM for ArchComponentsTest
 */

@Dao
public interface NoteRoomDao {

    @Query("SELECT * FROM NoteRoom ORDER BY id DESC")
    LiveData<List<NoteRoom>> getAllNotesLastCreatedFirst();

    @Query("SELECT * FROM NoteRoom ORDER BY lastModified DESC")
    LiveData<List<NoteRoom>> getAllNotesLastModifiedFirst();

    @Query("SELECT * FROM NoteRoom ORDER BY lastModified DESC")
    DataSource.Factory<Integer, NoteRoom> getNotesPagedLastModifiedFirst();

    @Insert
    void add(NoteRoom noteRoom);

    @Query("DELETE FROM NoteRoom")
    void deleteAll();

    @Query("DELETE FROM NoteRoom WHERE id = :id")
    void delete(int id);

    @Update(onConflict = REPLACE)
    void update(NoteRoom noteRoom);

}
