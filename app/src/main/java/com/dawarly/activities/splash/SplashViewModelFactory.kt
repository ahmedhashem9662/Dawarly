package com.dawarly.activities.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dawarly.MyApplication

class SplashViewModelFactory (var application: MyApplication) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(application) as T
    }
}
