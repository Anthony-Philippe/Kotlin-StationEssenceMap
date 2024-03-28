package org.isen.fuelnav2.model.impl

import org.apache.logging.log4j.kotlin.Logging
import java.beans.*
import kotlin.properties.Delegates
import org.isen.fuelnav2.model.*
import org.isen.fuelnav2.view.impl.SelectedStationView

class DefaultStationModel: IStationModel {
    companion object : Logging
    private val support = PropertyChangeSupport(this)
    private val selectedView : SelectedStationView? = null
    private var searchResultStationEssences: List<StationEssence> by Delegates.observable(emptyList()) { _, _, _ ->
        logger.info("stationInformation property change, notify observer")
        support.firePropertyChange(IStationModel.STATION_DATA, null, searchResultStationEssences)
    }

    private var selectedStation: StationEssence? by Delegates.observable(null) {
            _, oldValue, newValue -> logger.info("update selectedStation")
        support.firePropertyChange(IStationModel.SELECTED_DATA, oldValue, newValue)
        if(newValue != null){
            selectedView?.close()
            SelectedStationView(newValue)
        }
    }

    override fun register(datatype: String?, listener: PropertyChangeListener) {
        if(datatype == null) {
            support.addPropertyChangeListener(IStationModel.STATION_DATA, listener)
            support.addPropertyChangeListener(IStationModel.SELECTED_DATA, listener)
            support.addPropertyChangeListener(IStationModel.SEARCH_DATA, listener)
        } else support.addPropertyChangeListener(datatype, listener)
    }

    override fun findStation(Parameters : Parameters?, source: IStationModel.Map) {
        if(Parameters == null) {
            if(search == null) throw IllegalArgumentException("searchParameters is null")
        } else search = StationSearch(Parameters)
        searchResultStationEssences = listOf()
        currentPosition = CarteDataCurrentPosition()
        search!!.executeSearch(source)
        searchResultStationEssences = search!!.stations!!
    }


    private var search: StationSearch? by Delegates.observable(null) { _, _, _ ->
        logger.info("update search")
        support.firePropertyChange(IStationModel.SEARCH_DATA, null, search)
    }

    private var currentPosition: CarteData? by Delegates.observable(null) { _, _, _ ->
        logger.info("update currentPosition")
        support.firePropertyChange(IStationModel.POSITION_DATA, null, currentPosition)
    }

    override fun changeCurrentSelection(id: String?) { selectedStation = searchResultStationEssences.find { it.id == id }}
    override fun unregister(listener: PropertyChangeListener) {}
}