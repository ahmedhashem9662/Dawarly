package com.dawarly.activities.placeDetails

import androidx.lifecycle.MutableLiveData
import com.dawarly.MyApplication
import com.dawarly.activities.baseActivity.BaseViewModel
import com.dawarly.model.ResponseDetails
import com.dawarly.model.ResultModel
import com.dawarly.networkConnetcion.APIClient
import com.dawarly.networkConnetcion.APIInterface
import com.example.dawarly.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class PlaceDetailsViewModel(application: MyApplication) : BaseViewModel(application) {
    lateinit var observer: Observer

    var imageView = MutableLiveData<String>()
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
        getPlaceDetailsFromServer()
    }

    fun setData() {
        name.value = selectedPlace!!.name
        address.value = application.getString(R.string.address) + selectedPlace!!.formatted_address
        phone.value = application.getString(R.string.phone) + selectedPlace!!.formatted_phone_number
        location.value =
            application.getString(R.string.location) + selectedPlace!!.geometry!!.location!!.lat
                .toString() + " , " + selectedPlace!!.geometry!!.location!!.lng
        vicinity.value = application.getString(R.string.vicinity) + selectedPlace!!.vicinity
        website.value = application.getString(R.string.website) + selectedPlace!!.website
        rating.value = application.getString(R.string.rating) + selectedPlace!!.rating
        internationalPhone.value =
            application.getString(R.string.international_phone) + selectedPlace!!.international_phone_number
        url.value = application.getString(R.string.url) + selectedPlace!!.url
    }

    fun getPlaceDetailsFromServer() {
        observer.showProgressDialog(true)
        val apiInterface: APIInterface = APIClient().getClient().create(APIInterface::class.java)
        val params = HashMap<String, String>()
        params["place_id"] = selectedPlace!!.id!!
        params["key"] = "AIzaSyDveBIyZRQzBqgJlJRib0yD9F_-gyazW3c"
        val call: Call<ResponseDetails?>? = apiInterface.getPlaceDetailsFromServer(params)
        call!!.enqueue(object : Callback<ResponseDetails?> {
            override fun onResponse(
                call: Call<ResponseDetails?>,
                response: Response<ResponseDetails?>
            )
            {
                observer.showProgressDialog(false)
                val responseDetails: ResponseDetails? = response.body()
                setData()
                selectedPlace = responseDetails!!.result
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

