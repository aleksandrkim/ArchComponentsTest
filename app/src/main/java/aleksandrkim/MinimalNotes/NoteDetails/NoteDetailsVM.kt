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
    val db = AppDatabase.getDb(application)

    val currentNote: MutableLiveData<Note> = MutableLiveData()
    val color: MutableLiveData<Int> = MutableLiveData()

    fun setCurrentNote(id: Int, savedColor: Int?) {
        AsyncTask.execute {
            currentNote.postValue(if (id == -1) Note() else db.noteRoomDao().getNoteById(id))
        }
        savedColor?.let { color.value = it }
    }

    fun addOrUpdateCurrentNote() {
        color.value?.let { currentNote.value!!.color = it }
        AsyncTask.execute { db.noteRoomDao().addOrUpdate(currentNote.value!!) }
    }

    fun removeAllObs(owner: LifecycleOwner) {
        currentNote.removeObservers(owner)
    }

    companion object {
        const val TAG = "NoteDetailsVM"
    }
}