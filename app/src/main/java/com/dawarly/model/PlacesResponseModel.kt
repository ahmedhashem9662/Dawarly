package com.dawarly.model

import java.io.Serializable
import java.util.*

class PlacesResponseModel : Serializable {

    var results: ArrayList<ResultModel>? = null
    var next_page_token: String? = null
    var reference: String? = null
}