package com.dawarly.details

import java.io.Serializable

class Reviews : Serializable {

    var author_name: String? = null
    var author_url: String? = null
    var language: String? = null
    var profile_photo_url: String? = null
    var rating = 0.0
    var relative_time_description: String? = null
    var text: String? = null
    var time = 0
}