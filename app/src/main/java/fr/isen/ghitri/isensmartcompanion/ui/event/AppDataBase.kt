package fr.isen.ghitri.isensmartcompanion.ui.History

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.isen.ghitri.isensmartcompanion.ui.History.ConversationHistoryDao
import fr.isen.ghitri.isensmartcompanion.ui.History.ConversationHistoryEntity
import fr.isen.ghitri.isensmartcompanion.ui.event.EventDAO
import fr.isen.ghitri.isensmartcompanion.ui.event.EventModele
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase



@Database(
    entities = [EventModele::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun notifEventDao(): EventDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "events_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
