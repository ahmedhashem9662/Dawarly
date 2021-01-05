package com.dawarly.details

import java.io.Serializable
import java.util.*

class OpeningHours : Serializable {

    var periods: ArrayList<String>? = null
    var weekday_text: ArrayList<String>? = null
}