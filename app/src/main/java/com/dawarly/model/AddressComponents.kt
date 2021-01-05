package com.dawarly.model

import java.io.Serializable
import java.util.*

class AddressComponents : Serializable {

    var long_name: String? = ""
    var short_name: String? = ""
    var types: ArrayList<String>? = null
}