package aleksandrkim.MinimalNotes.NoteFeed

import aleksandrkim.MinimalNotes.Db.AppDatabase
import aleksandrkim.MinimalNotes.Db.Note
import aleksandrkim.MinimalNotes.Utils.Colors
import aleksandrkim.MinimalNotes.Utils.Event
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.os.AsyncTask
import android.util.Pair

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 11:37 PM for ArchComponentsTest
 */

class NotesFeedVM(application: Application) : AndroidViewModel(application) {

    private val db: AppDatabase = AppDatabase.getDb(this.getApplication())

    var allPagedNotes: LiveData<PagedList<Note>>? = null
        private set

    var swipedNote: MutableLiveData<Event<Pair<Int, Int>>> = MutableLiveData()
        private set

    fun subscribeToPagedNotes(pageSize: Int) {
        allPagedNotes = LivePagedListBuilder(db.noteRoomDao().getNotesPagedLastCreatedFirst(), pageSize).build()
    }

    fun removeAllObs(owner: LifecycleOwner) {
        allPagedNotes!!.removeObservers(owner)
        swipedNote.removeObservers(owner)
    }

    fun deleteNote(id: Int) {
        AsyncTask.execute { db.noteRoomDao().delete(id) }
    }

    fun deleteAllNotes() {
        AsyncTask.execute { db.noteRoomDao().deleteAll() }
    }

    fun swipe(noteId: Int, swipePosition: Int) {
        swipedNote.value = Event(Pair.create(noteId, swipePosition))
    }

    fun addSampleNotes(count: Int) {
        AsyncTask.execute {
            for (i in 0 until count) {
                val note = Note("Sample", LOREM_IPSUM, Colors.colors[i % Colors.colors.size])
                when (i) {
                    count - 1 -> note.title = ""
                    count - 2 -> note.body = ""
                }
                db.noteRoomDao().add(note)
            }
        }
    }

    companion object {
        val TAG = "NotesFeedVM"
        val LOREM_IPSUM =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore " +
                    "magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea " +
                    "commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat " +
                    "nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit " +
                    "anim id est laborum."

    }
}
