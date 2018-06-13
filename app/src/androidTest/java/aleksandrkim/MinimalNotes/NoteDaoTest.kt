package aleksandrkim.MinimalNotes

import aleksandrkim.MinimalNotes.Db.AppDatabase
import aleksandrkim.MinimalNotes.Db.Note
import aleksandrkim.MinimalNotes.Db.NoteDao
import aleksandrkim.MinimalNotes.Utils.Colors
import android.arch.persistence.room.Room
import android.graphics.Color
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Aleksandr Kim on 13 Jun, 2018 4:46 PM for ArchComponentsTest
 */

inline fun NoteDao.assertCountChanged(action: NoteDao.() -> Unit, dif: Int = 1) {
    val oldCount = this.count()
    action()
    assert(this.count() == oldCount + dif)
}

inline fun NoteDao.assertCountUnchanged(action: NoteDao.() -> Unit) {
    assertCountChanged(action, 0)
}

@RunWith(AndroidJUnit4::class)
open class NoteDaoTest {
    private lateinit var appDb: AppDatabase
    private lateinit var noteDao: NoteDao

    private val testSampleSize = 10

    private fun assertCountChanged(action: NoteDao.() -> Unit, dif: Int = 1) {
        addSampleNotes()
        noteDao.assertCountChanged(action, dif)
    }


    @Before
    fun initDb() {
        appDb = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
            .allowMainThreadQueries().build()
        noteDao = appDb.noteRoomDao()
    }

    @After
    fun closeDb() {
        appDb.close()
    }

    @Test
    fun addsSingleNote() {
        val note = Note("Sample title", "Body content text text a lot of it", Color.BLUE)

        noteDao.assertCountUnchanged { add(note) }
        assert(noteDao.getLatestNote().sameAs(note))
    }

    @Test
    fun updatesSingleNote() {
        addSampleNotes()
        val latestNote = noteDao.getLatestNote().apply {
            title = "new title"
            body = "new body"
            color = 123
        }
        addSampleNotes()

        noteDao.assertCountUnchanged({ update(latestNote) })
        assert(noteDao.getNoteById(latestNote.id)!!.title == "new title")
        assert(noteDao.getNoteById(latestNote.id)!!.body == "new body")
        assert(noteDao.getNoteById(latestNote.id)!!.color == 123)
    }

    @Test
    fun addsOrUpdatesNotes() {
        addSampleNotes()
        val note = Note("Sample title", "Body content text text a lot of it", Color.BLUE).also { noteDao.add(it) }

        noteDao.assertCountChanged({ addOrUpdate(note) })

        noteDao.assertCountUnchanged {
            getLatestNote().apply {
                body = "askdasfn"; color = -1
                addOrUpdate(this)
            }
        }
    }

    @Test
    fun deletesCorrectNote() {
        addSampleNotes()
        val oldCount = noteDao.count()
        val noteId = Note("Sample title", "Body content text text a lot of it", Color.BLUE)
            .let { noteDao.add(it) }.toInt()
        assert(noteDao.count() == oldCount + 1)
        addSampleNotes()

        noteDao.assertCountChanged({delete(noteId)}, -1)
        assert(noteDao.getNoteById(noteId) == null)
    }

    @Test
    fun deletesAllNotes() {
        addSampleNotes(testSampleSize)
        noteDao.assertCountChanged({ deleteAll() }, -testSampleSize)
    }

    private fun addSampleNotes(count: Int = testSampleSize) {
        for (i in 0..count)
            noteDao.add(Note(i.toString(), "Body content text text a lot of it $i",
                             Colors.colors[count % Colors.colors.size]))
    }

}