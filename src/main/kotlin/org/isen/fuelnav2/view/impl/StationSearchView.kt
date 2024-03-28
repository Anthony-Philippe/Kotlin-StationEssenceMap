package org.isen.fuelnav2.view.impl

import org.apache.logging.log4j.kotlin.Logging
import java.awt.*
import java.awt.event.*
import javax.swing.*
import javax.swing.border.Border
import java.beans.PropertyChangeEvent
import org.isen.fuelnav2.ctrl.StationController
import org.isen.fuelnav2.model.IStationModel
import org.isen.fuelnav2.model.impl.Parameters
import org.isen.fuelnav2.model.impl.StationSearch
import org.isen.fuelnav2.view.IStationView

class StationSearchView(private val controller: StationController): IStationView, ActionListener  {
    companion object : Logging
    private val interfaceView :JFrame
    private var search: Parameters = Parameters()
    private val searchBox = JTextField(20)
    private val listInfoView: StationInfoView = StationInfoView(controller)

    init {
        controller.registerViewToMap(this, listOf(IStationModel.SEARCH_DATA))
        interfaceView = JFrame("Sélection - Station").apply {
            isVisible = false
            contentPane = interfaceView()
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            this.title = title
            this.minimumSize = Dimension(420, 720)
        }
    }

    private fun interfaceView(): JPanel {
        val view = JPanel()
        view.layout = BorderLayout()
        view.add(barRechercher(), BorderLayout.NORTH)
        view.add(listInfoView, BorderLayout.WEST)
        listInfoView.preferredSize = Dimension(400, 0)
        return view
    }

    private fun barRechercher(): JPanel {
        val box = JPanel()
        box.layout = BoxLayout(box,BoxLayout.Y_AXIS)
        val lineBorder: Border = BorderFactory.createTitledBorder("Indiquer un lieu")
        searchBox.border = lineBorder
        box.add(searchBox)
        box.background = Color.WHITE
        box.add(confirmerRecherche(), BorderLayout.SOUTH)
        return box
    }

    private fun confirmerRecherche() : JPanel{
        val box2 = JPanel()
        box2.layout = BoxLayout(box2,BoxLayout.Y_AXIS)
        val button = JButton("Rechercher")
        button.addActionListener{
            search.searchStr = searchBox.text
            controller.searchStationInfo(search)
        }
        box2.add(button)
        box2.background = Color.WHITE
        return box2
    }

    override fun propertyChange(evt: PropertyChangeEvent?) {
        if(evt?.propertyName == IStationModel.SEARCH_DATA) {
            val search = evt.newValue as StationSearch
            if(search.search != null){ this.search = search.search!! }
        }
    }

    override fun display() { interfaceView.isVisible = true }
    override fun close() { interfaceView.isVisible = false }
    override fun actionPerformed(e: ActionEvent?){}
}