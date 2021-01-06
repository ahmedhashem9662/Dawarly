package com.dawarly.activities.placeDetails

import androidx.lifecycle.MutableLiveData
import com.dawarly.MyApplication
import com.dawarly.activities.baseActivity.BaseViewModel
import com.dawarly.model.ResponseDetails
import com.dawarly.model.ResultModel
import com.dawarly.connection.APIClient
import com.dawarly.connection.APIInterface
import com.example.dawarly.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PlaceDetailsViewModel(application: MyApplication) : BaseViewModel(application) {
    lateinit var observer: Observer

    var placePhoto = MutableLiveData<String>()
    var name = MutableLiveData<String>()
    var vicinity = MutableLiveData<String>()
    var location = MutableLiveData<String>()
    var address = MutableLiveData<String>()
    var phone = MutableLiveData<String>()
    var website = MutableLiveData<String>()
    var rating = MutableLiveData<String>()
    var internationalPhone = MutableLiveData<String>()
    var url = MutableLiveData<String>()
    var selectedPlace: ResultModel? = null

    init {
        placePhoto.value = ""
        name.value = ""
        vicinity.value = ""
        location.value = ""
        address.value = ""
        phone.value = ""
        website.value = ""
        rating.value = ""
        internationalPhone.value = ""
        url.value = ""
    }

    fun setData() {
        placePhoto.value = selectedPlace!!.icon
        name.value = selectedPlace!!.name
        address.value = selectedPlace!!.formatted_address
        phone.value = selectedPlace!!.formatted_phone_number
        location.value =
            selectedPlace!!.geometry!!.location!!.lat.toString() + " , " + selectedPlace!!.geometry!!.location!!.lng
        vicinity.value = selectedPlace!!.vicinity
        website.value = selectedPlace!!.website
        rating.value = selectedPlace!!.rating
        internationalPhone.value = selectedPlace!!.international_phone_number
        url.value = selectedPlace!!.url
    }

    fun getPlaceDetailsFromServer(SelectedPlace: ResultModel) {
        observer.showProgressDialog(true)
        val apiInterface: APIInterface = APIClient().getClient().create(APIInterface::class.java)
        val params = HashMap<String, String>()

        params["place_id"] = selectedPlace!!.id!!
        params["key"] = "AIzaSyDveBIyZRQzBqgJlJRib0yD9F_-gyazW3c"

        val call = apiInterface.getPlaceDetailsFromServer(params)
        call!!.enqueue(object : Callback<ResponseDetails?> {
            override fun onResponse(
                call: Call<ResponseDetails?>,
                response: Response<ResponseDetails?>
            ) {
                observer.showProgressDialog(false)
                val responseDetails = response.body()
                setData()
                selectedPlace = responseDetails!!.result!!
            }

            override fun onFailure(call: Call<ResponseDetails?>, t: Throwable) {
                observer.showProgressDialog(false)
            }
        })
    }

    interface Observer {
        fun showProgressDialog(isShow: Boolean)
    }
}

