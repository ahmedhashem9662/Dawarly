package com.dawarly.activities.splash

import android.os.Handler
import android.os.Looper
import com.dawarly.MyApplication
import com.dawarly.activities.baseActivity.BaseViewModel

class SplashViewModel(application: MyApplication) : BaseViewModel(application) {
    lateinit var observer: Observer

    init {
    }

    fun startWaitingTimer(){
        Handler(Looper.getMainLooper()).postDelayed({
            observer.openNextScreen()
        },3000)
    }

    interface Observer {
         fun openNextScreen()
    }
}



