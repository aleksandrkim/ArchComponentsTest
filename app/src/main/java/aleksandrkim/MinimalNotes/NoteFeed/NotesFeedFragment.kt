package aleksandrkim.MinimalNotes.NoteFeed

import aleksandrkim.MinimalNotes.HostActivity.NavigationActivity
import aleksandrkim.MinimalNotes.NoteDetails.NoteDetailsFragment
import aleksandrkim.MinimalNotes.R
import aleksandrkim.MinimalNotes.Utils.SwipeCallback
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.fragment_notes_feed.*

class NotesFeedFragment : Fragment(), NavigationActivity.BackEnabled {
    private lateinit var navigationActivity: NavigationActivity

    private val noteFeedViewModel by lazy { ViewModelProviders.of(this).get(NotesFeedVM::class.java) }

    private lateinit var adapterLayoutManager: LinearLayoutManager
    private lateinit var itemTouchHelper: ItemTouchHelper

    private lateinit var pagedAdapter: PagedFeedAdapter
    private var scrollToTop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        setRetainInstance(true);
        setHasOptionsMenu(true)
        initVM()
        prepareRecycler()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
//        val v = container.inflate(R.layout.fragment_notes_feed)
        val v = inflater.inflate(R.layout.fragment_notes_feed, container, false)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navigationActivity = requireActivity() as NavigationActivity
        navigationActivity.setTitle(R.string.note_feed)
        navigationActivity.setUpButton(false)
        setFab()
        setRecycler()
        observePagedList()
        observeSwipeEvent()
    }

    private fun prepareRecycler() {
        pagedAdapter = PagedFeedAdapter(
            { pos -> launchNoteComposeFragment(pagedAdapter.getNote(pos)!!.id) },
            {
                if (scrollToTop) {
                    adapterLayoutManager!!.scrollToPositionWithOffset(0, 0)
                    scrollToTop = false
                }
            })

        val swipeCallback = SwipeCallback(
            { viewHolder, direction -> noteFeedViewModel!!.swipe((viewHolder as NoteFeedVH).id, viewHolder.getLayoutPosition()) })

        itemTouchHelper = ItemTouchHelper(swipeCallback)
    }

    private fun observePagedList() {
        noteFeedViewModel!!.allPagedNotes!!.observe(this, Observer { noteRooms ->
            if (pagedAdapter!!.currentList != null && noteRooms != null &&
                pagedAdapter!!.currentList!!.size < noteRooms!!.size &&
                adapterLayoutManager!!.findFirstVisibleItemPosition() == 0
            )
                scrollToTop = true

            pagedAdapter!!.submitList(noteRooms)
        })
    }

    private fun observeSwipeEvent() {
        noteFeedViewModel!!.swipedNote.observe(this, Observer { integerEvent ->
            integerEvent?.getContentIfAvailable()?.let {
                showSnackbar(R.string.note_deleted, Snackbar.LENGTH_SHORT, R.string.undo,
                             { view -> pagedAdapter!!.notifyItemChanged(it.second) },
                             { noteFeedViewModel!!.deleteNote(it.first) })
            }
        }
        )
    }

    private fun showSnackbar(displayTextId: Int, duration: Int, actionTextId: Int,
                             onActionClicked: (View) -> Unit, onDismissed: () -> Unit) {
        Snackbar.make(coordinator, displayTextId, duration)
            .setAction(actionTextId, onActionClicked)
            .addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION)
                        onDismissed()
                    super.onDismissed(transientBottomBar, event)
                }
            })
            .show()
    }

    private fun initVM() {
        noteFeedViewModel!!.subscribeToPagedNotes(20)
    }

    private fun setFab() {
        fab!!.setOnClickListener { view -> launchNoteComposeFragment(-1) }
    }

    private fun setRecycler() {
        adapterLayoutManager = LinearLayoutManager(navigationActivity as Context)
        recyclerView!!.layoutManager = adapterLayoutManager
        recyclerView!!.adapter = pagedAdapter
        recyclerView!!.setHasFixedSize(true)
        itemTouchHelper!!.attachToRecyclerView(recyclerView)
    }

    private fun launchNoteComposeFragment(noteId: Int) {
        navigationActivity.launchWholeFragment(NoteDetailsFragment.newInstance(noteId), NoteDetailsFragment.TAG)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu!!.clear()
        inflater!!.inflate(R.menu.note_feed_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_erase_all -> {
                noteFeedViewModel!!.deleteAllNotes()
                return true
            }
            R.id.menu_add_20    -> {
                noteFeedViewModel!!.addSampleNotes(5)
                return true
            }
            else                -> return false
        }
    }

    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed() called")
        navigationActivity.finish()
    }

    override fun onDestroyView() {
        noteFeedViewModel!!.removeAllObs(this)
        super.onDestroyView()
    }

    companion object {
        val TAG = "NotesFeedFragment"

        fun newInstance(): NotesFeedFragment {
            val fragment = NotesFeedFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
