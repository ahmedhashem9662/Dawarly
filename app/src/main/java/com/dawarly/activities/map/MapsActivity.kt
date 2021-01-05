package com.dawarly.activities.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dawarly.activities.baseActivity.BaseActivity
import com.dawarly.activities.maps.MapsViewModelFactory
import com.dawarly.activities.placeDetails.PlaceDetailsActivity
import com.dawarly.model.PlacesResponseModel
import com.dawarly.model.ResultModel
import com.dawarly.networkConnetcion.APIClient
import com.dawarly.networkConnetcion.APIInterface
import com.dawarly.observer.OnShowMarkerDialog
import com.dawarly.sub.PopupShowMarkerDialog
import com.dawarly.util.ProgressDialogLoading
import com.example.dawarly.R
import com.example.dawarly.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class MapsActivity : BaseActivity
    (
    true, false, true, true, "Dawarly"
), OnMapReadyCallback, MapsViewModel.Observer {

    lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    lateinit var myLocation: Location
    lateinit var resultModels: ArrayList<ResultModel>
    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var placeTypes: Array<String>
    lateinit var placeTypesKey: Array<String>
    lateinit var adapter: ArrayAdapter<String>

    override fun doCreate() {
        binding = putContentView(R.layout.activity_maps) as ActivityMapsBinding
        binding.viewModel = ViewModelProvider(
            this, MapsViewModelFactory(this.application)
        ).get(MapsViewModel::class.java)
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this

     //   baseBinding.toolbar.overflowIcon = ContextCompat.getDrawable(this, R.drawable.menu)

        onPlanetSpinnerClick()

     //    Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.Map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        when (item.itemId) {
//            R.id.menu -> {
//                val intent = Intent(this, AboutActivity::class.java)
//                this.startActivity(intent)
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//        return true
//    }

    override fun onPlanetSpinnerClick() {
        placeTypes = resources.getStringArray(R.array.planets_Spinner)
        placeTypesKey = resources.getStringArray(R.array.planets_Spinner_key)
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, placeTypes)

        //setting the planets_Spinner to spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //if you want to set any action you can do in this listener
        binding.planetsSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.planetsSpinner.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
    }

    private fun putMarkerOnMap() {

    }

    var isZoomed = false
    override fun centerMapOnLocation(location: Location, title: String) {
        if (location != null) {
            val userLocation = LatLng(location.latitude, location.longitude)
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(userLocation).title(title))
            if (!isZoomed) mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(userLocation, 12f)
            ) else mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
            isZoomed = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 0, 0f, locationListener
                )
                myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
                centerMapOnLocation(myLocation, "My Location")
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.isBuildingsEnabled = true
        mMap.isTrafficEnabled
        mMap.uiSettings.isCompassEnabled

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mMap.isMyLocationEnabled = true
        putMarkerOnMap()
        mMap.setOnInfoWindowClickListener { marker ->
            showMarkerOptions("What to do", " for this place?")
        }

        // Zoom into users location
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                //  centerMapOnLocation(location, "My Location")
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
            override fun onProviderEnabled(s: String) {}
            override fun onProviderDisabled(s: String) {}
        }

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0f, locationListener
            )

            myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
            centerMapOnLocation(myLocation, "My Location")

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
    }

    override fun onSearchViewClick() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.Map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        binding.svLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val location: String = binding.svLocation.query.toString()
                var addressList: List<Address>? = null
                if (location != null || location != "") {
                    val geocoder = Geocoder(this@MapsActivity)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val address = addressList!![0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap.addMarker(MarkerOptions().position(latLng).title(location))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        mapFragment?.getMapAsync(this)
    }

    override fun putMarkersOnMap(resultModels: ArrayList<ResultModel>) {
        mMap.clear()
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(myLocation.latitude, myLocation.longitude)
            ).title("My Location").snippet("My current location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )
        for (i in resultModels.indices) {
            mMap.addMarker(
                MarkerOptions().position(
                    LatLng(
                        resultModels[i].geometry!!.location!!.lat,
                        resultModels[i].geometry!!.location!!.lng
                    )
                )
                    .title(resultModels[i].name).snippet(resultModels[i].vicinity)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            )
        }
    }

    override fun showMarkerOptions(title: String, message: String) {
        if (title == "My Location") return
        var popupShowMarkerDialog = PopupShowMarkerDialog()
        var bundle = Bundle()
        bundle.putSerializable("title", title)
        bundle.putSerializable("message", message)
        bundle.putSerializable("positiveButtonText", getString(R.string.placedetails))
        bundle.putSerializable("negativeButtonText", getString(R.string.drawpath))
        bundle.putSerializable("isShowPositiveButton", true)
        bundle.putSerializable("isShowNegativeButton", true)

        popupShowMarkerDialog.arguments = bundle
        popupShowMarkerDialog.setmListener(object : OnShowMarkerDialog {
            override fun onPositiveClicked() {
                val MarkerTitle = title
                val SelectedPlace: ResultModel =
                    binding.viewModel!!.getSelectedPlacePosition(MarkerTitle)!!
                if (SelectedPlace != null) {
                    val reference: String? = SelectedPlace.reference
                    if (reference != null && reference != "") {
                        val intent = Intent(this@MapsActivity, PlaceDetailsActivity::class.java)
                        intent.putExtra("SelectedPlace", SelectedPlace)
                        startActivity(intent)
                    } else
                        Toast.makeText(
                            this@MapsActivity,
                            "Not Found Any Information",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }

            override fun onNegativeClicked() {
                val MarkerTitle: String = title
                val SelectedPlace = binding.viewModel!!.getSelectedPlacePosition(MarkerTitle)
                SelectedPlace?.let { binding.viewModel!!.getDrawPath(it) }

                if (MarkerTitle != null) {
                    if (MarkerTitle == "My Location") {
                        Toast.makeText(this@MapsActivity, "My Current Location", Toast.LENGTH_LONG)
                            .show()
                    } else {
                    }
                }
                showProgressDialog(false)
            }
        })
        popupShowMarkerDialog.isCancelable = false
        popupShowMarkerDialog.show(supportFragmentManager, "PopupAskMessageDialog")
    }

    override fun showProgressDialog(isShow: Boolean) {
        if (isShow) {
            ProgressDialogLoading.show(this@MapsActivity)
        } else {
            ProgressDialogLoading.dismiss(this@MapsActivity)
        }
    }

    override fun onMapClick() {
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    override fun onSatelliteClick() {
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
    }

    override fun onHybridClick() {
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    }

    fun search(view: View) {
        binding.viewModel!!.observer.showProgressDialog(true)
        val apiInterface: APIInterface = APIClient().getClient().create(APIInterface::class.java)
        var params = HashMap<String, String>()
        params.put("location", myLocation.latitude.toString() + "," + myLocation.longitude)
        var radius: Int = binding.edtRadius.text.toString().toInt() * 1000
        params.put("radius", radius.toString())
        params.put(
            "type", placeTypesKey.get(binding.planetsSpinner.selectedItemPosition)
                .toLowerCase(java.util.Locale("en"))
        )
        params.put("key", "AIzaSyDveBIyZRQzBqgJlJRib0yD9F_-gyazW3c")
        var call: Call<PlacesResponseModel?>? = apiInterface.getNearByPlaces(params)
        call!!.enqueue(object : Callback<PlacesResponseModel?> {
            override fun onResponse(
                call: Call<PlacesResponseModel?>,
                response: Response<PlacesResponseModel?>
            ) {
                binding.viewModel!!.observer.showProgressDialog(false)
                val placesResponseModel: PlacesResponseModel? = response.body()
                resultModels = placesResponseModel!!.results!!
                putMarkersOnMap(resultModels)
            }

            override fun onFailure(call: Call<PlacesResponseModel?>, t: Throwable) {
                binding.viewModel!!.observer.showProgressDialog(false)
            }
        })
    }
}


