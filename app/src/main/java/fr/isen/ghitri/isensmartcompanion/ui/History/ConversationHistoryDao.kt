package fr.isen.ghitri.isensmartcompanion.ui.History

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fr.isen.ghitri.isensmartcompanion.ui.History.ConversationHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationHistoryDao {

    @Insert
    suspend fun insertConversation(history: ConversationHistoryEntity): Long

    @Query("SELECT * FROM conversation_history ORDER BY timestamp DESC")
    fun getAllConversations(): Flow<List<ConversationHistoryEntity>>

    @Query("DELETE FROM conversation_history WHERE id = :id")
    suspend fun deleteConversationById(id: Int): Int

    @Query("DELETE FROM conversation_history")
    suspend fun clearHistory(): Int
}