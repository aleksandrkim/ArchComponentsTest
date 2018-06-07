package aleksandrkim.ArchComponentsTest.Db

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.IGNORE

/**
 * Created by Aleksandr Kim on 07 Jun, 2018 11:51 PM for ArchComponentsTest
 */

@Dao
abstract class BaseDao<T> {

    @Insert(onConflict = IGNORE)
    abstract fun add(t: T)

    @Update(onConflict = IGNORE)
    abstract fun update(t: T)

    @Transaction
    open fun addOrUpdate(t: T) {
        add(t)
        update(t)
    }

    @Delete
    abstract fun delete(t: T)

    @Insert(onConflict = IGNORE)
    open fun add(t: List<T>) {
        t.forEach(this::add)
    }

}
