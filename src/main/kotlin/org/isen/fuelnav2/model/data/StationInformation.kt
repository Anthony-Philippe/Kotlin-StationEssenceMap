package org.isen.fuelnav2.model.data

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import org.apache.logging.log4j.kotlin.logger
import org.isen.fuelnav2.model.impl.StationEssence
import org.isen.fuelnav2.model.impl.CarteData

data class StationInformation(val records:List<Record>) {
    class Deserializer: ResponseDeserializable<StationInformation> {
        override fun deserialize(content: String): StationInformation {
            return Gson().fromJson(content, StationInformation::class.java)
        }
    }
    data class Record( val fields: Fields )
    data class Fields(
        val adresse: String,
        val cp: String,
        val geom: List<Double>,
        val id: String,
        val pop: String,
        val prix_id: String,
        val prix_nom: String,
        val prix_valeur: Double,
        val services_service: String,
        val ville: String
    )
}

fun parseStationJSON(url: String): List<StationEssence> {
    val (request, response, result) = url.httpGet().responseObject(StationInformation.Deserializer())
    val (data, error) = result
    if(response.statusCode != 200) {
        logger("parseStationJSON").error("Error while parsing JSON : $error - ${response.statusCode}")
        throw error!!
    }
    val stationEssenceLists = data?.records?.groupBy { it.fields.id }?.map { (id, records) ->
        val carburantsList = records.map { record ->
            StationEssence.Carburants(record.fields.prix_id, record.fields.prix_valeur)
        }
        var servicesList : List<StationEssence.Service> = emptyList()
        if(records[0].fields.services_service != null && records[0].fields.services_service.isNotEmpty()){
            val servicesListString = records[0].fields.services_service.split("//")
            servicesList = servicesListString.map { serviceStr -> StationEssence.getServiceFromString(serviceStr) }
        }

        val carteData = CarteData(records[0].fields.geom[0], records[0].fields.geom[1], records[0].fields.adresse, records[0].fields.ville, records[0].fields.cp)
        StationEssence(carteData, null, carburantsList, servicesList, records[0].fields.id)
    }
    return stationEssenceLists ?: emptyList()
}
