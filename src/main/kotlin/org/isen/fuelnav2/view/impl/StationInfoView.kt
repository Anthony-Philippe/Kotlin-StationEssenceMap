package org.isen.fuelnav2.view.impl

import org.apache.logging.log4j.kotlin.Logging
import java.beans.*
import java.awt.*
import javax.swing.*
import org.isen.fuelnav2.ctrl.StationController
import org.isen.fuelnav2.model.impl.StationEssence
import org.isen.fuelnav2.model.IStationModel

class StationInfoView (private val controller: StationController): PropertyChangeListener,
    JPanel() {
    companion object : Logging
    private val stationEssenceList = JList<StationEssence>()
    private val stationEssenceListModel = DefaultListModel<StationEssence>()
    private val stationListView = StationInfoCellRender()
    private val stationListScroll = JScrollPane(stationEssenceList)

    init {
        controller.model.register(IStationModel.STATION_DATA, this)
        controller.model.register(IStationModel.SELECTED_DATA, this)
        stationEssenceList.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                val selectedStation = stationEssenceList.selectedValue
                if (selectedStation != null) controller.loadStationInfo(selectedStation.id)
                else controller.loadStationInfo(null)
            }
        }

        this.layout = BorderLayout(20, 20)
        stationEssenceList.model = stationEssenceListModel
        stationEssenceList.cellRenderer = stationListView
        this.add(stationListScroll, BorderLayout.CENTER)
    }

    override fun propertyChange(evt: PropertyChangeEvent?) {
        if (evt?.propertyName == IStationModel.STATION_DATA) {
            val stationList = evt.newValue as List<*>
            stationEssenceListModel.clear()
            stationList.forEach { stationEssenceListModel.addElement(it as StationEssence?) }
        }
    }
}