package org.isen.fuelnav2.ctrl

import org.isen.fuelnav2.model.IStationModel
import org.isen.fuelnav2.model.impl.Parameters
import org.isen.fuelnav2.view.IStationView

class StationController(val model: IStationModel) {
    private val views = mutableListOf<IStationView>()
    private var source: IStationModel.Map = IStationModel.Map.DATAgouv

    fun registerViewToMap(v: IStationView, datatypes: List<String>?) {
        if (!this.views.contains(v)) {
            this.views.add(v)
            if (datatypes != null) for (datatype in datatypes){ this.model.register(datatype,v) }
        }
    }

    fun searchStationInfo(parameters : Parameters? = null) {
        this.model.findStation(parameters, source) }
    fun loadStationInfo(id: String?){ model.changeCurrentSelection(id) }
    fun displayViews() { views.forEach { it.display() } }
}
