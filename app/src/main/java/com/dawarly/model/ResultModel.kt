package com.dawarly.model

import com.dawarly.details.OpeningHours
import com.dawarly.details.Reviews
import java.io.Serializable
import java.util.*

class ResultModel : Serializable {

    var adr_address: String? = null
    var business_status: String? = null
    var formatted_address: String? = null
    var formatted_phone_number = 0
    var icon: String? = null
    var id: String? = null
    var international_phone_number = 0
    var name: String? = null
    var opening: OpeningHours? = null
    var geoCodedWayPoints: GeoCodedWayPoints? = null
    var routes: Routes? = null
    var status: String? = null
    var place_id: String? = null
    var rating = 0.0
    var reference: String? = null
    var reviews: ArrayList<Reviews>? = null
    var scope: String? = null
    var types: ArrayList<String>? = null
    var url: String? = null
    var user_ratings_total = 0
    var utc_offset = 0
    var vicinity: String? = null
    var website: String? = null
    var point_of_interest: String? = null
    var establishment: String? = null
    var address: ArrayList<AddressComponents>? = null
    var geometry: GeometryModel? = null
    var opening_hours: OpeningHoursModel? = null
    var photos: ArrayList<PhotoModel>? = null
    var plus_code: PlusCodeModel? = null
    var SelectedPlacePosition: String? = null

}


