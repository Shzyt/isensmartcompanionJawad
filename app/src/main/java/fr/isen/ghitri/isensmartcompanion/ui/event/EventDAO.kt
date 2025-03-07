package fr.isen.ghitri.isensmartcompanion.ui.event

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.isen.ghitri.isensmartcompanion.ui.event.EventModele
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {

    @Insert
    suspend fun insertPinnedEvent(eventEntity: EventModele): Long

    @Query("DELETE FROM notif_events WHERE id = :id")
    suspend fun deleteByEventId(id: String): Int


    @Query("SELECT COUNT(*) FROM notif_events WHERE id = :id")
    fun isPinned(id: String): Flow<Int>


    @Query("SELECT * FROM notif_events")
    fun getAllPinnedEvents(): Flow<List<EventModele>>
}