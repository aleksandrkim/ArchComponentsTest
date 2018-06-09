package aleksandrkim.MinimalNotes.Db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 7:26 PM for ArchComponentsTest
 */

@Database(entities = arrayOf(Note::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteRoomDao(): NoteDao

    companion object {

        private var appDatabase: AppDatabase? = null

        fun getDb(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "AppDatabase").build()
            }
            return appDatabase!!
        }
    }


}
