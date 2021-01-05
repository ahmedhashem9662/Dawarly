package com.dawarly.activities.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dawarly.MyApplication
import com.dawarly.activities.map.MapsViewModel

class MapsViewModelFactory (var application: MyApplication) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MapsViewModel(application) as T
    }
}