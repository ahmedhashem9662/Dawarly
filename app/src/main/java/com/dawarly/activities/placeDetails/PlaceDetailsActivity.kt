package com.dawarly.activities.placeDetails

import androidx.lifecycle.ViewModelProvider
import com.dawarly.activities.baseActivity.BaseActivity
import com.dawarly.util.ProgressDialogLoading
import com.example.dawarly.R
import com.example.dawarly.databinding.ActivityPlaceDetailsBinding
import java.util.*

class PlaceDetailsActivity : BaseActivity(
    true, true, true, true, "Place Details"
), PlaceDetailsViewModel.Observer {

    lateinit var binding: ActivityPlaceDetailsBinding

    override fun doCreate() {
        binding = putContentView(R.layout.activity_place_details) as ActivityPlaceDetailsBinding
        binding.viewModel = ViewModelProvider(
            this, PlaceDetailsViewModelFactory(this.application)
        ).get(PlaceDetailsViewModel::class.java)
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this
    }


    override fun showProgressDialog(isShow: Boolean) {
        if (isShow) {
            ProgressDialogLoading.show(this@PlaceDetailsActivity)
        } else {
            ProgressDialogLoading.dismiss(this@PlaceDetailsActivity)
        }
    }
}


