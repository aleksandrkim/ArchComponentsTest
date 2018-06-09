package aleksandrkim.MinimalNotes.NoteDetails

import aleksandrkim.MinimalNotes.Db.AppDatabase
import aleksandrkim.MinimalNotes.Db.Note
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask

/**
 * Created by Aleksandr Kim on 27 May, 2018 11:21 PM for ArchComponentsTest
 */

class NoteDetailsVM(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDb(application)

    val color: MutableLiveData<Int> = MutableLiveData()
    lateinit var currentNote: Note
        private set

    fun setCurrentNote(id: Int, savedColor: Int?) {

        AsyncTask.execute {
            currentNote = if (id == -1)
                Note("", "", -1)
            else
                db.noteRoomDao().getNoteById(id)

            color.postValue(savedColor ?: currentNote.color)
        }
    }

    fun addOrUpdateCurrentNote() {
        currentNote.color = color.value!!
        AsyncTask.execute { db.noteRoomDao().addOrUpdate(currentNote) }
    }

    fun removeAllObs(owner: LifecycleOwner) {
        color.removeObservers(owner)
    }

    fun setColor(color: Int) {
        this.color.value = color
    }

    companion object {
        val TAG = "NoteDetailsVM"
    }
}