package org.isen.fuelnav2.model

import java.beans.PropertyChangeListener
import org.isen.fuelnav2.model.impl.Parameters

interface IStationModel {
    companion object{
        const val STATION_DATA = "station"
        const val SELECTED_DATA = "stationSelected"
        const val SEARCH_DATA = "search"
        const val POSITION_DATA = "currentPosition"
    }
    enum class Map(val urlStart: String, val urlEnd: String) {
        DATAgouv(
            "https://data.economie.gouv.fr/api/records/1.0/search/?dataset=prix-carburants-fichier-instantane-test-ods-copie&q=",
            "&rows=-1&facet=id&facet=cp&facet=pop&facet=adresse&facet=ville&facet=geom&facet=prix_id&facet=prix_valeur&facet=prix_nom&facet=services_service"
        )
    }

    fun register(datatype:String?,listener: PropertyChangeListener)
    fun unregister(listener: PropertyChangeListener)
    fun changeCurrentSelection(id:String?)
    fun findStation(parameters : Parameters?, source: Map)
}