package com.dawarly.activities.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dawarly.activities.baseActivity.BaseActivity
import com.dawarly.activities.maps.MapsViewModelFactory
import com.dawarly.activities.placeDetails.PlaceDetailsActivity
import com.dawarly.model.ResultModel
import com.dawarly.observer.OnShowMarkerDialog
import com.dawarly.sub.PopupShowMarkerDialog
import com.dawarly.util.ProgressDialogLoading
import com.example.dawarly.R
import com.example.dawarly.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException
import java.util.*

class MapsActivity : BaseActivity (
    true, false, true, true,
    "Dawarly", true, true), MapsViewModel.Observer {

    lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    lateinit var locationManager: LocationManager

    override fun doCreate() {
        binding = putContentView(R.layout.activity_maps) as ActivityMapsBinding
        binding.viewModel = ViewModelProvider(
            this, MapsViewModelFactory(this.application)
        ).get(MapsViewModel::class.java)
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this

        initSpinnerCategories()
        initMap()
    }

    var locationPermissionRequestCode = 1001

    private fun initMap() {
        //    Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.Map) as SupportMapFragment
        mapFragment.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(googleMap: GoogleMap?) {
                if (googleMap == null)
                    return
                mMap = googleMap
                setMapSettings()
                updateLocation()
                mMap.setOnInfoWindowClickListener { marker -> showMarkerOptions(marker) }
            }
        })

        binding.svLocation.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    var addressList: List<Address>? = null
                    if (binding.viewModel!!.keyWord.value!!.isNotEmpty()) {
                        val geocoder = Geocoder(this@MapsActivity)
                        try {
                            addressList =
                                geocoder.getFromLocationName(binding.viewModel!!.keyWord.value!!, 1)
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (addressList != null && addressList.isNotEmpty()) {
                            val address = addressList[0]
                            val latLng = LatLng(address.latitude, address.longitude)
                            mMap.addMarker(
                                MarkerOptions().position(latLng)
                                    .title(binding.viewModel!!.keyWord.value!!)
                            )
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                        }
                    }
                }
                return false
            }
        })
    }

    fun updateLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                locationPermissionRequestCode
            )
            return
        }
        mMap.isMyLocationEnabled = true

        // Zoom into users location
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 100, 0f
        ) {
            if (it != null) {
                binding.viewModel!!.myLocation = it
                centerMapOnLocation(binding.viewModel!!.myLocation!!)
            }
        }
    }

    fun setMapSettings() {
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.isBuildingsEnabled = true
        mMap.isTrafficEnabled
        mMap.uiSettings.isCompassEnabled
    }

    override fun initSpinnerCategories() {
        binding.viewModel!!.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // if you want to set any action you can do in this listener
        binding.planetsSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.viewModel!!.selectedCategoryIndex = position
                (parent!!.getChildAt(0) as TextView).setTextColor(Color.CYAN)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.planetsSpinner.adapter = binding.viewModel!!.adapter
    }

    var isFirstTime = false

    fun centerMapOnLocation(location: Location) {
        if (location != null) {
            if (::myMarker.isInitialized)
                myMarker.remove()
            myMarker = mMap.addMarker(
                MarkerOptions().position(LatLng(location.latitude, location.longitude))
                    .title(getString(R.string.my_location))
                    .snippet(getString(R.string.my_current_location))
            )
            if (isFirstTime) {
                isFirstTime = false
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(location.latitude, location.longitude), 12f
                    )
                )
                mMap.moveCamera(
                    CameraUpdateFactory.newLatLng(
                        LatLng(location.latitude, location.longitude)))
            }
        }
    }

    override fun onRequestPermissionsResult (requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == locationPermissionRequestCode
            && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                updateLocation()
            }
        }
    }

    lateinit var myMarker: Marker
    override fun putMarkersOnMap(resultModels: ArrayList<ResultModel>) {
        mMap.clear()
        if (::myMarker.isInitialized)
            myMarker.remove()
        myMarker = mMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    binding.viewModel!!.myLocation?.latitude!!,
                    binding.viewModel!!.myLocation?.longitude!!
                )
            ).title(getString(R.string.my_location))
                .snippet(getString(R.string.my_current_location))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
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
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
        }
    }

    override fun showMarkerOptions(marker: Marker) {
        if (title == getString(R.string.my_location)) return
        var popupShowMarkerDialog = PopupShowMarkerDialog()
        var bundle = Bundle()
        bundle.putSerializable("title", getString(R.string.what_do_you_want))
        bundle.putSerializable("message", getString(R.string.for_this_place))
        bundle.putSerializable("positiveButtonText", getString(R.string.placedetails))
        bundle.putSerializable("negativeButtonText", getString(R.string.drawpath))
        bundle.putSerializable("isShowPositiveButton", true)
        bundle.putSerializable("isShowNegativeButton", true)

        popupShowMarkerDialog.arguments = bundle
        popupShowMarkerDialog.setmListener(object : OnShowMarkerDialog {
            override fun onPositiveClicked() {
                val SelectedPlace: ResultModel =
                    binding.viewModel!!.getSelectedPlacePosition(marker.title)!!
                if (SelectedPlace != null) {
                    val reference: String? = SelectedPlace.reference
                    if (reference != null && reference != "") {
                        val intent = Intent(this@MapsActivity, PlaceDetailsActivity::class.java)
                        intent.putExtra("SelectedPlace", SelectedPlace)
                        startActivity(intent)
                    } else
                        Toast.makeText(
                            this@MapsActivity,
                            getString(R.string.not_found_any_information),
                            Toast.LENGTH_LONG
                        ).show()
                }
            }

            override fun onNegativeClicked() {
                binding.viewModel!!.getSelectedPlacePosition(marker.title)
                    .let { binding.viewModel!!.getDrawPath(it!!) }
            }
        })
        popupShowMarkerDialog.isCancelable = true
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
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    }

    override fun onTerrainClick() {
        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
    }

    override fun drawPath(route: List<LatLng>) {
        for (i in 0 until route.size - 1) {
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




