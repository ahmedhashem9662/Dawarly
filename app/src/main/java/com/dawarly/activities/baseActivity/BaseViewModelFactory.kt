package com.dawarly.activities.baseActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dawarly.MyApplication

class BaseViewModelFactory (var application: MyApplication) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BaseViewModel(application) as T
    }
}
