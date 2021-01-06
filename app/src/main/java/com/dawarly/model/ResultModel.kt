package com.dawarly.model

import com.dawarly.details.OpeningHours
import com.dawarly.details.Reviews
import java.io.Serializable
import java.util.*

class ResultModel : Serializable {

    var adr_address: String? = ""
    var business_status: String? = ""
    var formatted_address: String? = ""
    var formatted_phone_number: String? = ""
    var icon: String? = ""
    var id: String? = ""
    var international_phone_number: String? = ""
    var name: String? = ""
    var opening: OpeningHours? = null
    var geoCodedWayPoints: GeoCodedWayPoints? = null
    var routes: Routes? = null
    var status: String? = ""
    var place_id: String? = ""
    var rating: String? = ""
    var reference: String? = ""
    var reviews: ArrayList<Reviews>? = null
    var scope: String? = ""
    var types: ArrayList<String>? = null
    var url: String? = ""
    var user_ratings_total: String? = ""
    var utc_offset: String? = ""
    var vicinity: String? = ""
    var website: String? = ""
    var point_of_interest: String? = ""
    var establishment: String? = ""
    var address: ArrayList<AddressComponents>? = null
    var geometry: GeometryModel? = null
    var opening_hours: OpeningHoursModel? = null
    var photos: ArrayList<PhotoModel>? = null
    var plus_code: PlusCodeModel? = null
    var SelectedPlacePosition: String? = ""

}


