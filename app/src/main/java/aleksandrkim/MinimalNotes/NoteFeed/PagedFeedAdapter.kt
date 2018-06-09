package aleksandrkim.MinimalNotes.NoteFeed

import aleksandrkim.MinimalNotes.Db.Note
import aleksandrkim.MinimalNotes.R
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by Aleksandr Kim on 20 Apr, 2018 4:25 PM for ArchComponentsTest
 */

class PagedFeedAdapter(private val recyclerItemClickListener: RecyclerItemClickListener,
                       private val onListUpdatedListener: OnListUpdatedListener) :
    PagedListAdapter<Note, NoteFeedVH>(Note.DIFF_ITEM_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteFeedVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.note_feed_row, parent, false)
        val noteFeedVH = NoteFeedVH(v)
        v.setOnClickListener { recyclerItemClickListener.onItemClick(noteFeedVH.adapterPosition) }
        return noteFeedVH
    }

    override fun onCurrentListChanged(currentList: PagedList<Note>?) {
        super.onCurrentListChanged(currentList)
        onListUpdatedListener.onListUpdated(currentList?.run { size } ?: 0)
    }

    fun getNote(position: Int): Note? {
        return getItem(position)
    }

    override fun onBindViewHolder(holder: NoteFeedVH, position: Int) {
        val note = getItem(position)
        note?.let { holder.bind(it) } ?: holder.clear()
    }

    companion object {
        val TAG = "PagedFeedAdapter"
    }
}
