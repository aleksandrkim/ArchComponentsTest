package aleksandrkim.MinimalNotes.NoteDetails

import aleksandrkim.MinimalNotes.HostActivity.NavigationActivity
import aleksandrkim.MinimalNotes.R
import aleksandrkim.MinimalNotes.Utils.Colors
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_note_details.*

class NoteDetailsFragment : Fragment(), NavigationActivity.BackEnabled {
    private var noteId = -1
    private lateinit var navigationActivity: NavigationActivity

    private val noteDetailsViewModel: NoteDetailsVM by lazy { ViewModelProviders.of(this).get(NoteDetailsVM::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

        this.noteId = arguments?.getInt(KEY_NOTE_ID, -1) ?: -1

        noteDetailsViewModel.setCurrentNote(noteId, savedInstanceState?.getInt(KEY_NOTE_COLOR))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) observeAvailableNote()

        navigationActivity = requireActivity() as NavigationActivity
        navigationActivity.setTitle(R.string.new_note)
        navigationActivity.setUpButton(true)
    }

    private fun observeAvailableNote() {
        noteDetailsViewModel.currentNote.observe(this, Observer {
            title.setText(it?.title)
            body.setText(it?.body)
            noteDetailsViewModel.color.value = it?.color
        })
    }

    private fun changeColorTag() {
        val builder = AlertDialog.Builder(navigationActivity as Context)
        builder.setTitle(R.string.choose_color)
            .setItems(Colors.colorTitles) { _, which -> noteDetailsViewModel.color.value = Colors.colors[which] }
        builder.create().show()
        noteDetailsViewModel.currentNote.value!!.title = "Trigger"
    }

    private fun saveNote() {
        updateCurrentNote()
        if (!noteDetailsViewModel.currentNote.value!!.isBlank()) {
            noteDetailsViewModel.addOrUpdateCurrentNote()
        } else {
            Toast.makeText(activity, getString(R.string.cannot_save_empty_note), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateCurrentNote() {
        noteDetailsViewModel.currentNote.value?.title = title.text.toString()
        noteDetailsViewModel.currentNote.value?.body = body.text.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.note_compose_menu, menu)
        noteDetailsViewModel.color.observe(this, Observer {
            Log.d(TAG, "color observed")
            menu.getItem(0).icon.setTint(it ?: -1)
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home    -> {
                saveNote()
                return false
            }
            R.id.menu_color_pick -> {
                changeColorTag()
                return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        saveNote()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState: ")
        outState.putInt(KEY_NOTE_COLOR, noteDetailsViewModel.currentNote.value!!.color)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        noteDetailsViewModel.removeAllObs(this)
        super.onDestroyView()
    }

    companion object {
        const val TAG = "ComposeFragment"
        const val KEY_NOTE_ID = "noteIdParam"
        const val KEY_NOTE_COLOR = "noteColor"

        fun newInstance(noteId: Int): NoteDetailsFragment {
            val fragment = NoteDetailsFragment()

            val args = Bundle()
            args.putInt(KEY_NOTE_ID, noteId)
            fragment.arguments = args
            return fragment
        }
    }
}
