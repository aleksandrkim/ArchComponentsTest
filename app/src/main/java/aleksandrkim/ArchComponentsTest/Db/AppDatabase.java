package aleksandrkim.ArchComponentsTest.Db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 7:26 PM for ArchComponentsTest
 */

@Database(entities = {NoteRoom.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    public abstract NoteRoomDao noteRoomDao();

    public static AppDatabase getDb(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, "AppDatabase").build();
        }
        return appDatabase;
    }


}
