package com.dawarly.model

import java.io.Serializable
import java.util.*

class Legs : Serializable {

    var distance: Distance? = null
    var duration: Duration? = null
    var end_address: String? = null
    var endLocation: EndLocation? = null
    var start_address: String? = null
    var startLocation: StartLocation? = null
    var steps: Steps? = null
    var traffic_speed_entry: ArrayList<String>? = null
    var via_waypoint: ArrayList<String>? = null
}