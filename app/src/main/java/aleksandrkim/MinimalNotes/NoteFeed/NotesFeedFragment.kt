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
import android.view.*
import kotlinx.android.synthetic.main.fragment_notes_feed.*

class NotesFeedFragment : Fragment(), NavigationActivity.BackEnabled {
    private lateinit var navigationActivity : NavigationActivity
    private val noteFeedViewModel by lazy { ViewModelProviders.of(this).get(NotesFeedVM::class.java) }

    private lateinit var adapterLayoutManager: LinearLayoutManager

    private lateinit var itemTouchHelper: ItemTouchHelper

    private lateinit var pagedAdapter: PagedFeedAdapter
    private var scrollToTop = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)

        prepareRecycler()

        noteFeedViewModel.subscribeToPagedNotes(20)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notes_feed, container, false)
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
                    adapterLayoutManager.scrollToPositionWithOffset(0, 0)
                    scrollToTop = false
                }
            })

        itemTouchHelper = ItemTouchHelper(SwipeCallback(
            { viewHolder, _ -> noteFeedViewModel.swipe((viewHolder as NoteFeedVH).id, viewHolder.getLayoutPosition()) }))
    }

    private fun observePagedList() {
        noteFeedViewModel.allPagedNotes.observe(this, Observer { noteRooms ->
            if (noteRooms != null && pagedAdapter.currentList != null &&
                pagedAdapter.currentList!!.size < noteRooms.size &&
                adapterLayoutManager.findFirstVisibleItemPosition() == 0
            ) scrollToTop = true

            pagedAdapter.submitList(noteRooms)
        })
    }

    private fun observeSwipeEvent() {
        noteFeedViewModel.swipedNote.observe(this, Observer { integerEvent ->
            integerEvent?.getContentIfAvailable()?.let {
                showSnackbar(R.string.note_deleted, Snackbar.LENGTH_SHORT, R.string.undo,
                             { _ -> pagedAdapter.notifyItemChanged(it.second) },
                             { noteFeedViewModel.deleteNote(it.first) })
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
                    if (event != DISMISS_EVENT_ACTION)
                        onDismissed()
                    super.onDismissed(transientBottomBar, event)
                }
            })
            .show()
    }

    private fun setFab() {
        fab.setOnClickListener { launchNoteComposeFragment(-1) }
    }

    private fun setRecycler() {
        adapterLayoutManager = LinearLayoutManager(navigationActivity as Context)
        recyclerView.layoutManager = adapterLayoutManager
        recyclerView.adapter = pagedAdapter
        recyclerView.setHasFixedSize(true)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun launchNoteComposeFragment(noteId: Int) {
        navigationActivity = requireActivity() as NavigationActivity
        navigationActivity.launchWholeFragment(NoteDetailsFragment.newInstance(noteId), NoteDetailsFragment.TAG)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.note_feed_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_erase_all -> {
                noteFeedViewModel.deleteAllNotes()
                true
            }
            R.id.menu_add_20 -> {
                noteFeedViewModel.addSampleNotes(5)
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        navigationActivity.finish()
    }

    override fun onDestroyView() {
        noteFeedViewModel.removeAllObs(this)
        super.onDestroyView()
    }

    companion object {
        const val TAG = "NotesFeedFragment"

        fun newInstance(): NotesFeedFragment {
            val fragment = NotesFeedFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}
