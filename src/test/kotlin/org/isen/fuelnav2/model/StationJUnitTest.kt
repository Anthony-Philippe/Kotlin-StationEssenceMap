package org.isen.fuelnav2.model

import org.apache.logging.log4j.kotlin.Logging
import org.isen.fuelnav2.model.data.parseStationJSON
import org.isen.fuelnav2.model.impl.StationEssence
import kotlin.test.*

class StationInformationJUnitTest {
    companion object : Logging

    @org.junit.Test
    fun getFirstStationInformationFromJson() {
        val source = IStationModel.Map.DATAgouv
        val url = source.urlStart + source.urlEnd
        val list : List<StationEssence> = parseStationJSON(url)
        assertFalse(list.isEmpty(),"No stations detected from the JSON Parser")
        assertTrue(list.size > 1000, "Missing stations from JSON Parser Result")
    }
}