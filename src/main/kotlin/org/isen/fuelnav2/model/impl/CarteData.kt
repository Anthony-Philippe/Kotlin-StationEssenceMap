package org.isen.fuelnav2.model.impl

import com.github.kittinunf.fuel.httpGet

open class CarteData(
    var latitude: Double,
    var longitude: Double,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null) {
    override fun toString(): String {
        return "$address $postalCode $city"
    }
}

class CarteDataCurrentPosition : CarteData(0.0, 0.0) {
    private val URLwKey = "https://api.ipgeolocation.io/ipgeo?apiKey=d83c78f9417e45b3a6ed169b9056861c"
    init {
        val jsonString = URLwKey.httpGet().responseString().third.get()
        val json = org.json.JSONObject(jsonString)
        this.latitude = json.getDouble("latitude")
        this.longitude = json.getDouble("longitude")
    }
}