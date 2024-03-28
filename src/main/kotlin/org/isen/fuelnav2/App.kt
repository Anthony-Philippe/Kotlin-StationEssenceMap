package org.isen.fuelnav2

import org.isen.fuelnav2.ctrl.StationController
import org.isen.fuelnav2.model.impl.DefaultStationModel
import org.isen.fuelnav2.model.impl.Parameters
import org.isen.fuelnav2.view.map.StationCarteView
import org.isen.fuelnav2.view.impl.StationSearchView

fun main() {
    val IStationModel = DefaultStationModel()
    val controller = StationController(IStationModel)
    StationCarteView(controller)
    StationSearchView(controller)
    controller.displayViews()
    controller.searchStationInfo(Parameters("toulon", null, null, null, null))
}