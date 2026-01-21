package no.emil.organizer.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import no.emil.organizer.data.models.TodoInstance
import no.emil.organizer.data.models.TodoWithInstance

@Dao
interface TodoInstanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(instance: TodoInstance): Long

    @Update
    suspend fun update(instance: TodoInstance)

    @Query("""
        SELECT * FROM todo_instances
        WHERE completed = 0
        ORDER BY dueTimestamp ASC
    """)
    fun getUpcoming(): Flow<List<TodoInstance>>

    @Query("""
        SELECT * FROM todo_instances
        WHERE dueTimestamp BETWEEN :start AND :end
        ORDER BY dueTimestamp ASC
    """)
    fun getByDateRange(
        start: Long,
        end: Long
    ): Flow<List<TodoInstance>>

    @Query("""
        UPDATE todo_instances
        SET completed = 1,
        completedTimestamp = :timestamp
        WHERE id = :id
    """)
    suspend fun markCompleted(
        id: Long,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE todo_instances
        SET completed = 0,
        completedTimestamp = NULL
        WHERE id = :id
    """)
    suspend fun markUncompleted(
        id: Long
    )

    @Transaction
    @Query("""
        SELECT * FROM todo_instances
        WHERE completed = 0
            OR completedTimestamp >= :startOfToday
        ORDER BY dueTimestamp ASC
    """)
    fun getUpcomingWithItem(startOfToday: Long): Flow<List<TodoWithInstance>>

    @Query("""
        UPDATE todo_instances
        SET dueTimestamp = :newDue
        WHERE id = :id
    """)
    suspend fun updateDueTimestamp(id: Long, newDue: Long)
}