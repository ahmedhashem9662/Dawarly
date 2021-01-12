package com.dawarly

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.dawarly.util.LocaleHelper
import com.dawarly.util.Preferences

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Preferences.initPreference(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.updateLocale(base))
        MultiDex.install(base)
    }
}

