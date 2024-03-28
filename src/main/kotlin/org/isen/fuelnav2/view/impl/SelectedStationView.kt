package org.isen.fuelnav2.view.impl

import org.apache.logging.log4j.kotlin.Logging
import javax.swing.*
import java.awt.*
import java.awt.event.*
import java.beans.PropertyChangeEvent
import org.isen.fuelnav2.model.impl.StationEssence
import org.isen.fuelnav2.view.IStationView

class SelectedStationView (stationEssence: StationEssence): IStationView, ActionListener {
    companion object : Logging
    private val interfaceView: JFrame
    private var selectedStationEssence: StationEssence = stationEssence

    init {
        interfaceView = JFrame("Station d'Essence - Information").apply {
            isVisible = true
            interfaceView(this)
            defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
            this.title = title
            this.minimumSize = Dimension(450, 350)
        }
    }

    private fun interfaceView (view: JFrame): JFrame {
        view.layout = GridLayout(2, 1)
        view.background = Color.WHITE
        view.add(carburantView())
        view.add(servicesView())
        return view
    }

    private fun carburantView (): JPanel {
        val carburantBox = JPanel()
        carburantBox.layout = GridLayout(selectedStationEssence.carburants.size, 1)
        carburantBox.border = BorderFactory.createTitledBorder("Carburants disponibles")
        carburantBox.background = Color.WHITE
        for (carburant in selectedStationEssence.carburants) {
            val carburantLabel = JLabel()
            carburantLabel.text = "${carburant.type} : ${carburant.price}€"
            carburantBox.add(carburantLabel)
        }
        return carburantBox
    }

    private fun servicesView (): JPanel {
        val servicesBox = JPanel()
        servicesBox.layout = GridLayout(selectedStationEssence.services.size, 2)
        servicesBox.border = BorderFactory.createTitledBorder("Services disponibles")
        servicesBox.background = Color.WHITE
        for (service in selectedStationEssence.services) {
            val serviceLabel= JLabel()
            serviceLabel.text = service.value
            servicesBox.add(serviceLabel)
        }
        return servicesBox
    }

    override fun display() { interfaceView.isVisible = true }
    override fun close() { interfaceView.isVisible = false }
    override fun propertyChange(evt: PropertyChangeEvent?) { this.close() }
    override fun actionPerformed(e: ActionEvent?) {}
}