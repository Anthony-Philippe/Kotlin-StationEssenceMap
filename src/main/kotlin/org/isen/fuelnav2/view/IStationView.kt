package org.isen.fuelnav2.view

import java.beans.PropertyChangeListener

interface IStationView:PropertyChangeListener {
    fun display()
    fun close()
}