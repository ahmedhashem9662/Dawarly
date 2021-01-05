package com.dawarly.model

import java.io.Serializable
import java.util.*

class Routes : Serializable{

    var copyrights: String? = null
    var overview_polyline: OverViewPolyLine? = null
    var summary: String? = null
    var warnings: ArrayList<String>? = null
    var waypoint_order: ArrayList<String>? = null

}