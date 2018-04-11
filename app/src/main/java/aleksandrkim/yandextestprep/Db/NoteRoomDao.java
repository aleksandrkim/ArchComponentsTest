package aleksandrkim.yandextestprep.Db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:41 PM for YandexTestPrep
 */

@Dao
public interface NoteRoomDao {

    @Query("SELECT * FROM NoteRoom ORDER BY id DESC")
    LiveData<List<NoteRoom>> getAllNotesLastCreatedFirst();

    @Query("SELECT * FROM NoteRoom ORDER BY lastModified DESC")
    LiveData<List<NoteRoom>> getAllNotesLastModifiedFirst();

    @Query("SELECT * from noteroom where id = :id")
    LiveData<NoteRoom> getNote(int id);

    @Query("SELECT * from noteroom where id = :id")
    MutableLiveData<NoteRoom> getMutableNote(int id);

    @Insert
    void insert(NoteRoom noteRoom);

    @Query("DELETE FROM NoteRoom")
    void deleteAll();

    @Query("DELETE FROM NoteRoom WHERE id = :id")
    void delete(int id);

    @Query("UPDATE NoteRoom SET title = :title, content = :content, color = :color where id = :id")
    void update(int id, String title, String content, int color);

    @Update(onConflict = REPLACE)
    void update(NoteRoom noteRoom);

}
