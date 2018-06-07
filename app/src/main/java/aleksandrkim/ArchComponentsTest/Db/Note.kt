package aleksandrkim.ArchComponentsTest.Db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.support.v7.util.DiffUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 6:27 PM for ArchComponentsTest
 */

@Entity(indices = [
    Index("id"),
    Index(value = ["title", "content"], name = "search_text")])
data class Note(var title: String = "", var content: String = "", var color: Int = -1) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var lastModified: Long = Calendar.getInstance().timeInMillis
    var createdTime: Long = Calendar.getInstance().timeInMillis

    @Ignore
    private val simpleDateFormat = SimpleDateFormat("dd MMM", Locale("Ru"))

    val lastModifiedString: String
        get() = simpleDateFormat.format(lastModified)

    val createdTimeString: String
        get() = simpleDateFormat.format(createdTime)

    private fun equals(obj: Note): Boolean = (this.id == obj.id && this.title == obj.title && this.content == obj.content
            && this.color == obj.color && this.lastModified == obj.lastModified)

    companion object {

        val DIFF_ITEM_CALLBACK: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.equals(newItem)
        }
    }
}
