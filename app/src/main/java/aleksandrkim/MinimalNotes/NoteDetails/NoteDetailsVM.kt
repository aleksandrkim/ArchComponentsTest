package aleksandrkim.MinimalNotes.NoteDetails

import aleksandrkim.MinimalNotes.Db.AppDatabase
import aleksandrkim.MinimalNotes.Db.Note
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.os.AsyncTask
import android.util.Log

/**
 * Created by Aleksandr Kim on 27 May, 2018 11:21 PM for ArchComponentsTest
 */

class NoteDetailsVM(application: Application) : AndroidViewModel(application) {
    val db = AppDatabase.getDb(application)

    val currentNote: MutableLiveData <Note> = MutableLiveData()
    val color : MutableLiveData<Int> = MutableLiveData()

    fun setCurrentNote(id: Int, savedColor: Int?) {
        AsyncTask.execute {
            if (id == -1) {
                currentNote.postValue(Note())
            } else {
                currentNote.postValue(db.noteRoomDao().getNoteById(id))

                Log.d(TAG, "fetched " + currentNote.value?.toString())
                Log.d(TAG, "setCurrentNote " + id)
                Log.d(TAG, "title " + currentNote.value?.title)
            }
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
        val TAG = "NoteDetailsVM"
    }
}