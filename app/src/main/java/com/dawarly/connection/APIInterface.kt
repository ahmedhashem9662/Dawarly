package com.dawarly.networkConnetcion

import com.dawarly.model.PlacesResponseModel
import com.dawarly.model.ResponseDetails
import com.dawarly.model.ResponseDirection
import retrofit2.http.GET
import retrofit2.http.QueryMap
import java.util.*

interface APIInterface {

    @GET("place/nearbysearch/json")
    fun getNearByPlaces(@QueryMap params: HashMap<String, String>): retrofit2.Call<PlacesResponseModel?>?

    @GET("place/details/json")
    fun getPlaceDetailsFromServer(@QueryMap params: HashMap<String, String>): retrofit2.Call<ResponseDetails?>?

    @GET("directions/json")
    fun getDrawPath(@QueryMap params: HashMap<String, String>): retrofit2.Call<ResponseDirection?>?

}
