package com.dawarly.activities.map

import android.location.*
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import com.dawarly.MyApplication
import com.dawarly.activities.baseActivity.BaseViewModel
import com.dawarly.decode.DecodePoly
import com.dawarly.model.PlacesResponseModel
import com.dawarly.model.ResponseDirection
import com.dawarly.model.ResultModel
import com.dawarly.connection.APIClient
import com.dawarly.connection.APIInterface
import com.example.dawarly.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

open class MapsViewModel(application: MyApplication) : BaseViewModel(application) {
    lateinit var observer: Observer

    var keyWord = MutableLiveData<String>()
    var radius = MutableLiveData<String>()
    var map = MutableLiveData<Boolean>()
    var satellite = MutableLiveData<Boolean>()
    var hybrid = MutableLiveData<Boolean>()
    var isShowMap = MutableLiveData<Boolean>()
    var myLocation: Location? = null
    var resultModels: ArrayList<ResultModel> = ArrayList()

    var placeTypes: Array<String>
    var placeTypesKey: Array<String>
    var adapter: ArrayAdapter<String>
    var selectedCategoryIndex = -1

    init {
        keyWord.value = ""
        radius.value = ""
        map.value = false
        satellite.value = false
        hybrid.value = false
        isShowMap.value = false

        placeTypes = application.resources.getStringArray(R.array.planets_Spinner)
        placeTypesKey = application.resources.getStringArray(R.array.planets_Spinner_key)
        adapter = ArrayAdapter<String>(application.applicationContext, android.R.layout.simple_spinner_item, placeTypes)
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
                observer.showProgressDialog(false)
                val responseDirection: ResponseDirection? = response.body()
                if (responseDirection?.routes != null && responseDirection.routes!!.isNotEmpty()
                ) {
                    val route: List<LatLng>? =
                        responseDirection.routes!![0].overview_polyline!!.points?.let {
                            DecodePoly().decodePoly(it)
                        }.also { observer.drawPath(it!!) }
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

    fun onSearchClick() {
        observer.showProgressDialog(true)
        val apiInterface: APIInterface = APIClient().getClient().create(APIInterface::class.java)
        val params = HashMap<String, String>()
        if (myLocation == null)
            return
        params["location"] = myLocation!!.latitude.toString() + "," + myLocation!!.longitude
        if (radius.value!!.isNotEmpty()) {
            val radius: Int = radius.value.toString().toInt() * 1000
            params["radius"] = radius.toString()
        }
        if (selectedCategoryIndex != -1)
            params["type"] = placeTypesKey[selectedCategoryIndex].toLowerCase(Locale("en"))
        params["key"] = "AIzaSyDveBIyZRQzBqgJlJRib0yD9F_-gyazW3c"
        val call: Call<PlacesResponseModel?>? = apiInterface.getNearByPlaces(params)
        call!!.enqueue(object : Callback<PlacesResponseModel?> {
            override fun onResponse(
                call: Call<PlacesResponseModel?>,
                response: Response<PlacesResponseModel?>
            ) {
                observer.showProgressDialog(false)
                val placesResponseModel: PlacesResponseModel? = response.body()
                resultModels = placesResponseModel!!.results!!
                observer.putMarkersOnMap(placesResponseModel.results!!)
            }

            override fun onFailure(call: Call<PlacesResponseModel?>, t: Throwable) {
                observer.showProgressDialog(false)
            }
        })
    }

    interface Observer {
        fun initSpinnerCategories()
        fun onMapClick()
        fun onSatelliteClick()
        fun onTerrainClick()
        fun drawPath(route: List<LatLng>)
        fun putMarkersOnMap(resultModels: ArrayList<ResultModel>)
        fun showMarkerOptions(marker: Marker)
        fun showProgressDialog(isShow: Boolean)
    }
}


