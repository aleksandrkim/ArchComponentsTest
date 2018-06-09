package aleksandrkim.MinimalNotes.Db

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:41 PM for ArchComponentsTest
 */

@Dao
abstract class NoteDao : BaseDao<Note>() {

    @Query("SELECT * FROM Note ORDER BY createdTime DESC")
    abstract fun getNotesPagedLastCreatedFirst(): DataSource.Factory<Int, Note>

    @Query("SELECT * FROM Note WHERE id = :id")
    abstract fun getNoteById(id: Int) : Note

    @Query("DELETE FROM Note")
    abstract fun deleteAll()

    @Query("DELETE FROM Note WHERE id = :id")
    abstract fun delete(id: Int)

}
