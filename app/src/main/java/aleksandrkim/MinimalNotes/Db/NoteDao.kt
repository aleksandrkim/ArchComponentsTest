package aleksandrkim.MinimalNotes.Db

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:41 PM for ArchComponentsTest
 */

@Dao
abstract class NoteDao : BaseDao<Note>() {

    @Query("SELECT * FROM " + Note.TABLE_NAME + " ORDER BY createdTime DESC")
    abstract fun getNotesPagedLastCreatedFirst(): DataSource.Factory<Int, Note>

    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE id = :id")
    abstract fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM " + Note.TABLE_NAME + " ORDER BY createdTime DESC LIMIT 1")
    abstract fun getLatestNote(): Note

    @Query("DELETE FROM " + Note.TABLE_NAME)
    abstract fun deleteAll()

    @Query("DELETE FROM " + Note.TABLE_NAME + " WHERE id = :id")
    abstract fun delete(id: Int)

    @Query("SELECT COUNT(*) FROM " + Note.TABLE_NAME)
    abstract override fun count(): Int

}
