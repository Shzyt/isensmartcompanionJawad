package fr.isen.ghitri.isensmartcompanion.ui.event

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import androidx.room.PrimaryKey
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "notif_events")
data class EventModele(
    @PrimaryKey(autoGenerate = true) val pinnedId: Int = 0,
    @SerializedName("category") val category: String,
    @SerializedName("date") val date: String,
    @SerializedName("description") val description: String,
    @SerializedName("id") val id: String,
    @SerializedName("location") val location: String,
    @SerializedName("title") val title: String
) : Parcelable
