package com.dawarly.activities.about

import com.dawarly.MyApplication
import com.dawarly.activities.baseActivity.BaseViewModel

open class AboutViewModel (application: MyApplication) : BaseViewModel(application) {
    lateinit var observer: Observer

    init {

    }

    interface Observer {
    }
}



