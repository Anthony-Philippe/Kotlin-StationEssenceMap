package org.isen.fuelnav2.view.map

import org.apache.logging.log4j.kotlin.Logging
import javax.swing.*
import java.awt.*
import java.awt.event.*
import java.beans.PropertyChangeEvent
import org.isen.fuelnav2.ctrl.StationController
import org.isen.fuelnav2.model.impl.StationEssence
import org.isen.fuelnav2.model.impl.CarteData
import org.isen.fuelnav2.model.IStationModel
import org.isen.fuelnav2.view.IStationView

class StationCarteView(controller: StationController) : IStationView, ActionListener {
    companion object : Logging
    private val interfaceView: JFrame
    private var carteImage : Image? = null
    private val carteView: ViewCarte = ViewCarte()
    private var carteLocalisation : List<CarteData>? = null
    private var defaultStationsCarte : DefaultStationsCarte? = null

    init {
        controller.registerViewToMap(this, listOf(IStationModel.STATION_DATA, IStationModel.SELECTED_DATA))
        defaultStationsCarte = DefaultStationsCarte()
        defaultStationsCarte!!.carteUpdate(this)

        interfaceView = JFrame("Carte des Stations").apply {
            contentPane = interfaceView()
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            this.minimumSize = Dimension(980, 720)
        }
    }

    private fun interfaceView(): JPanel {
        val result = JPanel()
        result.layout = BorderLayout(20, 20)
        result.add(carteView, BorderLayout.CENTER)
        return result
    }

    override fun propertyChange(evt: PropertyChangeEvent) {
        logger.debug("property change")
        if (evt.propertyName == IStationModel.STATION_DATA) {
            val stationEssenceList = evt.newValue as List<StationEssence>
            if(stationEssenceList.isNotEmpty()) {
                carteLocalisation = stationEssenceList.map { Station -> Station.carteData }
                defaultStationsCarte?.defSize(carteView.width, carteView.height)
                defaultStationsCarte?.clearStations()
                defaultStationsCarte?.addStations(carteLocalisation!!.map { geoPoint ->
                    Triple(geoPoint.latitude, geoPoint.longitude, geoPoint.address!!)})
            }
        }
        if(evt.propertyName == IStationModel.SELECTED_DATA){

                defaultStationsCarte?.defSize(carteView.width, carteView.height)
                val selectedStation = evt.newValue as StationEssence?
                defaultStationsCarte?.setSelectedStation(selectedStation?.carteData)
        }
        if(evt.propertyName == "Image" && evt.newValue != null){
            carteImage = evt.newValue as Image
            carteView.defStationsMap(carteImage!!)
        }
    }

    override fun display() { interfaceView.isVisible = true }
    override fun close() { interfaceView.isVisible = false }
    override fun actionPerformed(p0: ActionEvent?){}
}

class ViewCarte : JPanel() {
    private var carteStations: Image? = null
    fun defStationsMap(carteSt: Image) {
        this.carteStations = carteSt
        repaint()
    }
    override fun paint(g: Graphics) { if (carteStations != null) g.drawImage(carteStations, 0, 0, this) }
}