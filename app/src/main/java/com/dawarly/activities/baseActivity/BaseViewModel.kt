package com.dawarly.activities.baseActivity

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.dawarly.MyApplication

open class BaseViewModel(var application: MyApplication) : AndroidViewModel(application) {
    lateinit var baseObserver: Observer

    var toolbar = MutableLiveData<Boolean>()
    var isShowAppBar = MutableLiveData<Boolean?>()
    var activityName = MutableLiveData<String>()
    var isShowBackIcon= MutableLiveData <Boolean?>()
    var isShowAppImage= MutableLiveData <Boolean?>()
    var isShowActivityName= MutableLiveData<Boolean?>()

    init {
        toolbar.value = false
        isShowAppBar.value = false
        activityName.value = ""
        isShowBackIcon.value = false
        isShowAppImage.value = false
        isShowActivityName.value = false
    }

    interface Observer {
        fun setAppBar()
        fun onBack()
    }
}

