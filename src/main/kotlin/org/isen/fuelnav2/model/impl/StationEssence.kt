package org.isen.fuelnav2.model.impl

import kotlin.properties.Delegates

class StationEssence(
    val carteData: CarteData,
    val name: String? = null,
    val carburants: List<Carburants>,
    val services: List<Service>,
    val id: String) {
    var distance: Double? = null

    override fun toString(): String {
        return "Station(geoPoint=$carteData, name=$name, carburants=$carburants, services=$services, id=$id, distance=$distance)"
    }

    class Carburants() {
        var price by Delegates.notNull<Double>()
        lateinit var type : CarburantType
        constructor(id: String, price: Double) : this() {
            this.price = price
            type = getCarburantTypeFromString(id)
        }
        override fun toString(): String { return type.name + " : " + price }
        enum class CarburantType(val id: String){ GAZOLE("1"), SP95("2"), SP98("3"), GPLC("4"), E10("5"), E85("6") }

        companion object{
            fun getCarburantTypeFromString(id: String): CarburantType {
                return when(id) {
                    "1" -> CarburantType.GAZOLE
                    "2" -> CarburantType.SP95
                    "3" -> CarburantType.SP98
                    "4" -> CarburantType.E10
                    "5" -> CarburantType.E85
                    "6" -> CarburantType.GPLC
                    else -> throw IllegalArgumentException("Unknown carburant type")
                }
            }
        }
    }

    fun getSearchableString(): String {
        var searchableString = ""
        for(carburant in carburants) searchableString += carburant.type.name + " "
        for(service in services) searchableString += service.value + " "
        return searchableString + " $carteData "
    }

    enum class Service(val value: String) {
        SELL_GAS("Gaz (Butane, Propane)"),
        AIR_PUMP( "Station de gonflage"),
        ATM("Distributeur de billets automatique"),
        FOOD_STORE("Boutique d'alimentation"),
        TRUCK_PARKING("Zone pour poids lourds"),
        ATM24("Automate 24H/24"),
        NON_FOOD_STORE("Boutique"),
        AUTOMATIC_WASH("Lavage automatique"),
        CASH_MACHINE("Distributeur de billets"),
        PUBLIC_TOILETS("Toilettes publiques"),
        MANUAL_WASH("Lavage manuel"),
        TAKE_AWAY("Restauration à emporter"),
        CAR_RENTAL("Location de véhicule"),
        PARCEL_DELIVERY("Point relais"),
        LAUNDRY("Laverie"),
        ADDITIVES( "Vente d'additifs pour carburants"),
        RESTAURANT("Restaurant"),
        REPAIR("Services réparation et entretien"),
        WIFI("Wifi disponible"),
        HEATING_OIL("Vente de fioul"),
        LAMP_OIL ("Vente de pétrole pour lampe"),
        BABY_AREA("Espace pour bébé"),
        BAR("Bar"),
        ELECTRICITY("Bornes électriques"),
        SHOWER("Douches publiques"),
        CAMPER_PARKING("Zone pour camping-cars"),
        GNV("Vente de méthane"),
        UNKNOWN("Unknown");
    }

    companion object {
        fun getServiceFromString(service: String): Service {
            return Service.values().find { it.value == service } ?: Service.UNKNOWN
        }
    }
}