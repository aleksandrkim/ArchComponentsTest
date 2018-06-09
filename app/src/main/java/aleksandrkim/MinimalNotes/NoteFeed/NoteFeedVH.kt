package aleksandrkim.MinimalNotes.NoteFeed

import aleksandrkim.MinimalNotes.Db.Note
import aleksandrkim.MinimalNotes.R
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

/**
 * Created by Aleksandr Kim on 20 Apr, 2018 4:37 PM for ArchComponentsTest
 */

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

class NoteFeedVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var id: Int = 0
        private set
    private val title: TextView = itemView.findViewById(R.id.et_title)
    private val content: TextView = itemView.findViewById(R.id.et_content)
    private val date: TextView = itemView.findViewById(R.id.tv_date)
    private val colorStrip: View = itemView.findViewById(R.id.color_strip)

    fun bind(note: Note) {
        id = note.id
        if (note.title.isEmpty()) title.gone()
        else title.text = note.title

        if (note.content.isEmpty()) content.gone()
        else content.text = note.content

        content.text = note.content
        date.text = note.createdTimeString
        colorStrip.background.setTint(note.color)
    }

    fun clear() {
        itemView.invalidate()
        title.invalidate()
        content.invalidate()
        date.invalidate()
        colorStrip.invalidate()
    }
}