package fr.isen.ghitri.isensmartcompanion.ui.event

import fr.isen.ghitri.isensmartcompanion.ui.event.EventModele
import retrofit2.http.GET

interface EventCollect {
    @GET("events.json") // L'endpoint de l'API
    suspend fun getEvents(): List<EventModele>
}