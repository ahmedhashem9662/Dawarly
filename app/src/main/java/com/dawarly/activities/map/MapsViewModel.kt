package com.dawarly.activities.map

import android.graphics.Color
import android.location.*
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.dawarly.MyApplication
import com.dawarly.activities.baseActivity.BaseViewModel
import com.dawarly.decode.DecodePoly
import com.dawarly.model.PlacesResponseModel
import com.dawarly.model.ResponseDirection
import com.dawarly.model.ResultModel
import com.dawarly.networkConnetcion.APIClient
import com.dawarly.networkConnetcion.APIInterface
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

open class MapsViewModel(application: MyApplication) : BaseViewModel(application) {
    lateinit var observer: Observer

    var searchView = MutableLiveData<String>()
    var planetSpinner = MutableLiveData<String>()
    var radius = MutableLiveData<String>()
    var search = MutableLiveData<Boolean>()
    var map = MutableLiveData<Boolean>()
    var satellite = MutableLiveData<Boolean>()
    var hybrid = MutableLiveData<Boolean>()
    var isShowMap = MutableLiveData<Boolean>()
    var myLocation: Location? = null
    lateinit var mMap: GoogleMap
    var resultModels: ArrayList<ResultModel> = ArrayList()
    lateinit var placeTypes: Array<String>
    lateinit var placeTypesKey: Array<String>

    init {
        searchView.value = ""
        planetSpinner.value = ""
        radius.value = ""
        search.value = false
        map.value = false
        satellite.value = false
        hybrid.value = false
        isShowMap.value = false
    }

    fun getDrawPath(SelectedPlace: ResultModel) {
        observer.showProgressDialog(true)
        val apiInterface: APIInterface = APIClient().getClient().create(APIInterface::class.java)
        val params = HashMap<String, String>()
        params["origin"] = myLocation!!.latitude.toString() + "," + myLocation!!.longitude
        params["destination"] = SelectedPlace.geometry!!.location!!.lat
            .toString() + "," + SelectedPlace.geometry!!.location!!.lng
        params["key"] = "AIzaSyDveBIyZRQzBqgJlJRib0yD9F_-gyazW3c"
        val call: Call<ResponseDirection?>? = apiInterface.getDrawPath(params)
        call!!.enqueue(object : Callback<ResponseDirection?> {
            override fun onResponse(
                call: Call<ResponseDirection?>,
                response: Response<ResponseDirection?>
            ) {
                observer.showProgressDialog(true)
                val responseDirection: ResponseDirection? = response.body()
                if (responseDirection?.routes != null && responseDirection.routes!!.isNotEmpty()
                ) {
                    val route: List<LatLng>? =
                        responseDirection.routes!![0].overview_polyline!!.points?.let {
                            DecodePoly().decodePoly(it)
                        }
                    for (i in 0 until route!!.size - 1) {
                        val src = route[i]
                        val dest = route[i + 1]
                        try {
                            //here is where it will draw the polyline in your map
                            mMap.addPolyline(
                                PolylineOptions()
                                    .add(
                                        LatLng(src.latitude, src.longitude),
                                        LatLng(dest.latitude, dest.longitude)
                                    ).width(10f).color(Color.BLUE).geodesic(true)
                            )
                        } catch (e: NullPointerException) {
                        } catch (e2: Exception) {
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseDirection?>, t: Throwable) {
                observer.showProgressDialog(false)
            }
        })
    }

    fun getSelectedPlacePosition(Title: String): ResultModel? {
        if (resultModels != null) {
            for (i in resultModels.indices) {
                val TitleName: String = resultModels[i].name!!
                if (TitleName == Title) {
                    return resultModels[i]
                }
            }
        }
        return null
    }

//    fun onSearchClick() {
//        observer.showProgressDialog(true)
//        val apiInterface: APIInterface = APIClient().getClient().create(APIInterface::class.java)
//        val params = HashMap<String, String>()
//        params["location"] = myLocation!!.latitude.toString() + "," + myLocation!!.longitude
//        val radius: Int = radius.toString().toInt() * 1000
//        params["radius"] = radius.toString()
//        params["type"] = placeTypesKey[planetSpinner.value!!.length].toLowerCase(Locale("en"))
//        params["key"] = "AIzaSyDveBIyZRQzBqgJlJRib0yD9F_-gyazW3c"
//        val call: Call<PlacesResponseModel?>? = apiInterface.getNearByPlaces(params)
//        call!!.enqueue(object : Callback<PlacesResponseModel?> {
//            override fun onResponse(
//                call: Call<PlacesResponseModel?>,
//                response: Response<PlacesResponseModel?>
//            ) {
//                observer.showProgressDialog(false)
//                val placesResponseModel: PlacesResponseModel? = response.body()
//                resultModels = placesResponseModel!!.results!!
//                observer.putMarkersOnMap(placesResponseModel.results!!)
//            }
//
//            override fun onFailure(call: Call<PlacesResponseModel?>, t: Throwable) {
//                observer.showProgressDialog(false)
//            }
//        })
//
//    }


    interface Observer {
        fun onMapReady(googleMap: GoogleMap)
        fun centerMapOnLocation(location: Location, title: String)
        fun onSearchViewClick()
        fun onPlanetSpinnerClick()
        fun onMapClick()
        fun onSatelliteClick()
        fun onHybridClick()
        fun putMarkersOnMap(resultModels: ArrayList<ResultModel>)
        fun showMarkerOptions(title: String, message: String)
        fun showProgressDialog(isShow: Boolean)
    }
}

