package com.dawarly.activities.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dawarly.MyApplication

class AboutViewModelFactory (var application: MyApplication) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AboutViewModel(application) as T
    }
}