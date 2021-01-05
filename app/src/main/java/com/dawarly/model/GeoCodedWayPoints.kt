package com.dawarly.model

import java.io.Serializable
import java.util.*

class GeoCodedWayPoints : Serializable {

    var geocoder_status: String? = null
    var place_id: String? = null
    var types: ArrayList<String>? = null

}