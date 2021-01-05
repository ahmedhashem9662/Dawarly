package com.dawarly.model

import java.io.Serializable
import java.util.*

class PhotoModel : Serializable {

    var height = 0
    var width = 0
    var photo_reference: String? = null
    var html_attributions: ArrayList<String>? = null
}
