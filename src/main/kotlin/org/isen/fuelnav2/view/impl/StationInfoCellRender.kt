package org.isen.fuelnav2.view.impl

import java.awt.*
import javax.swing.*
import org.isen.fuelnav2.model.impl.StationEssence

class StationInfoCellRender:JLabel(), ListCellRenderer<StationEssence> {
    companion object {
        private val stationLogo = ImageIcon(ImageIcon(this::class.java.getResource(("/station_icon.png"))).image.getScaledInstance(50, 50, Image.SCALE_DEFAULT))
    }

    override fun getListCellRendererComponent(
        list: JList<out StationEssence>?, value: StationEssence?, index: Int, isSelected: Boolean, cellHasFocus: Boolean): Component {
        if (isSelected) {
            background = list?.selectionBackground
            foreground = list?.selectionForeground
        } else {
            background = list?.background
            foreground = list?.foreground
        }
        val component = this
        component.layout = BorderLayout()
        component.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

        if (value is StationEssence) {
            val stationLogoEssence = mutableListOf<Triple<ImageIcon, StationEssence.Carburants.CarburantType, String>>()
            text = "${value.carteData.address}${value.carteData.city?.uppercase()}"
            icon = stationLogo
            val panel = JPanel(FlowLayout(FlowLayout.RIGHT))
            panel.background = Color(0, 0, 0, 0)
            stationLogoEssence.forEach { panel.add(JLabel(it.first)) }
            component.removeAll()
            component.add(panel, BorderLayout.SOUTH)
        }
        return component
    }
}