package com.dawarly.activities.placeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dawarly.MyApplication

class PlaceDetailsViewModelFactory (var application: MyApplication) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlaceDetailsViewModel(application) as T
    }
}