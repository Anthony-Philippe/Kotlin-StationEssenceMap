package org.isen.fuelnav2.model.impl

import org.isen.fuelnav2.model.IStationModel
import org.isen.fuelnav2.model.data.parseStationJSON

data class Parameters(
    var searchStr: String? = null,
    var services: MutableList<StationEssence.Service>? = null,
    var carburants: StationEssence.Carburants.CarburantType? = null,
    var maxPrice: Double? = null,
    val maxDistance: Int? = null,
)

class StationSearch (var search: Parameters? = null) {
    private var hasBeenSearched = false
    var stations: List<StationEssence>? = null

    init { if (search == null) search = Parameters() }

    fun executeSearch(source: IStationModel.Map) {
        DefaultStationModel.logger.info("findStationInformation $source")
        println("new search : $search")

        if(search == null) {
            throw IllegalArgumentException("searchParameters is null")
            return
        }

        this.stations = when(source){
            IStationModel.Map.DATAgouv -> parseStationJSON(source.urlStart + search!!.searchStr + source.urlEnd)
        }

        DefaultStationModel.logger.info("findStationInformation $source done, ${stations?.size} stations found")
        println("findStationInformation $source done, ${stations?.size} stations found")

        val searchWords = search!!.searchStr?.split(" ")
        val searchWordsWithAttributes = searchWords?.map {
            var isWordACity = false
            var isWordAnAddress = false
            var isWordAPostalCode = false
            var isWordAService = false
            var isWordAGasType = false

            stations?.forEach { station ->
                if(station.carteData.city.equals(it, true)) isWordACity = true
                if(station.carteData.address.equals(it, true)) isWordAnAddress = true
                if(station.carteData.postalCode.equals(it, true)) isWordAPostalCode = true
                if(StationEssence.getServiceFromString(it) != StationEssence.Service.UNKNOWN) isWordAService = true
                if(StationEssence.Carburants.CarburantType.values().any { carburantType -> carburantType.name == it }) isWordAGasType = true
            }

            val attribute =
                if(isWordACity) "city"
                else if(isWordAnAddress) "address"
                else if(isWordAPostalCode) "postalCode"
                else if(isWordAService) "service"
                else if(isWordAGasType) "carburantType"
                else "unknown"
            listOf(it, attribute)
        }

        stations = stations?.filter { station ->
            var isOk = true
            if(search!!.services != null) isOk = isOk && search!!.services?.let { station.services.containsAll(it) } == true
            if(search!!.carburants != null) isOk = isOk && station.carburants.any { it.type == search!!.carburants }
            if(search!!.maxPrice != null) isOk = isOk && station.carburants.any { it.price <= search!!.maxPrice!! }

            searchWordsWithAttributes?.forEach { searchWordWithAttribute ->
                if(searchWordWithAttribute[1] == "city") isOk = isOk && station.carteData.city.equals(searchWordWithAttribute[0], ignoreCase = true)
                if(searchWordWithAttribute[1] == "address") isOk = isOk && station.carteData.address?.contains(searchWordWithAttribute[0], true) ?: false
                if(searchWordWithAttribute[1] == "postalCode") isOk = isOk && station.carteData.postalCode?.contains(searchWordWithAttribute[0], true) ?: false
                if(searchWordWithAttribute[1] == "service") isOk = isOk && station.services.any { it.name.contains(searchWordWithAttribute[0], true) }
                isOk = if(searchWordWithAttribute[1] == "carburantType") isOk && station.carburants.any { it.type.name.contains(searchWordWithAttribute[0], true) }
                else isOk && station.getSearchableString().contains(searchWordWithAttribute[0], true)
            }

            if(search!!.maxDistance != null)  isOk = isOk && station.distance != null && station.distance!! <= search!!.maxDistance!!
            isOk
        }
        if((stations?.size ?: 0) > 25) {
            stations = stations?.sortedBy { it.distance }
            stations = stations?.subList(0, 25)
        }
        println("${stations?.size} stations found")
        hasBeenSearched = true
    }
}