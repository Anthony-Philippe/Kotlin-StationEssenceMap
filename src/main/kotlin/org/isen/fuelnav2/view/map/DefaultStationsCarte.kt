package org.isen.fuelnav2.view.map

import java.net.URL
import java.awt.Image
import java.awt.geom.Point2D.distance
import java.beans.PropertyChangeSupport
import javax.imageio.ImageIO
import kotlin.math.*
import kotlin.properties.Delegates
import org.isen.fuelnav2.model.impl.CarteData

class DefaultStationsCarte {
    companion object {
        const val PREFIX_URL = "https://maps.geoapify.com/v1/staticmap"
        const val API_KEY = "f0775f620b0444fe8ea664f71854cda1"
    }
    private var height = 1080
    private var width = 1920
    private var stations = mutableListOf<Triple<Double, Double, String>>()
    private var selectedStation : Triple<Double, Double, String>? = null
    private val support = PropertyChangeSupport(this)
    private var image: Image? by Delegates.observable(null) { _, _, newValue -> support.firePropertyChange("Image", null, newValue) }

    fun carteUpdate(listener: StationCarteView) { support.addPropertyChangeListener(listener) }
    fun defSize(width: Int, height: Int) { this.width = width; this.height = height }
    fun addStations(stations: List<Triple<Double, Double, String>>) {
        this.stations.addAll(stations)
        generate()
    }
    fun setSelectedStation(station: CarteData?) {
        selectedStation = station?.let { Triple(it.latitude, it.longitude, it.address!!) }
        generate()
    }
    fun clearStations() { stations.clear() }

    private fun marker(coords:Pair<Double,Double>):String {
        return "lonlat:${coords.first},${coords.second};color:green;" + "icon:local_gas_station"
    }

    private fun selectedMarker(cords:Pair<Double,Double>):String {
        return "lonlat:${cords.first},${cords.second};color:red;" + "icon:local_gas_station"
    }

    private fun generate() {
        val posX = stations.map { it.first }.average()
        val posY = stations.map { it.second }.average()
        val maxDistance = stations.map { distance(it.second, it.first, posY, posX) }.max()
        val distance = 1 - (maxDistance.pow(1/4.5) / 5.8.pow(1/4.5))
        val zoom = (sqrt((height * height + width * width).toDouble()) * 0.0022) + (14 * distance)
        // Permet de définir un zoom en fonction des stations affichées
        val urlBuffer = StringBuilder(
            "$PREFIX_URL?style=carto&width=$width&height=$height" + "&center=lonlat:$posY,$posX&zoom=$zoom")

        urlBuffer.append("&marker=")
            .append(stations.joinToString("|") {
                if(it == selectedStation) selectedMarker(it.second to it.first)
                else marker(it.second to it.first)
            })
        urlBuffer.append("&apiKey=$API_KEY")

        val url = URL(urlBuffer.toString())
        image = ImageIO.read(url)
    }
}