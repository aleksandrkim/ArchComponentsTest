package aleksandrkim.MinimalNotes.Db

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
    Index(value = ["title", "body"], name = "search_text")])
data class Note(var title: String = "",
                var body: String = "",
                var color: Int = -1,
                var createdTime: Long = Calendar.getInstance().timeInMillis,
                @PrimaryKey(autoGenerate = true)
                var id: Int = 0) {

//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0
//    var createdTime: Long = Calendar.getInstance().timeInMillis

    @Ignore
    private val simpleDateFormat = SimpleDateFormat("dd MMM", Locale("Ru"))

    @Ignore
    val createdTimeString: String = simpleDateFormat.format(createdTime)

    private fun equals(obj: Note): Boolean =
        this.id == obj.id && this.title == obj.title && this.body == obj.body && this.color == obj.color

    fun isBlank() = title.isBlank() && body.isBlank()

    companion object {

        val DIFF_ITEM_CALLBACK: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.equals(newItem)
        }
    }
}
