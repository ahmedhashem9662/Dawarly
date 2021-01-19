package com.dawarly.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LocaleHelper {
    fun updateLocale(base: Context): Context {
        Preferences.initPreference(base)
        var local = ""
        //initialize preference
        if (Preferences.getApplicationLocale().compareTo("ar") == 0) {
            local = "ar"
        } else {
            local = "en"
        }
        Preferences.saveApplicationLocale(local)
        local.let {
            return if (it.isNotEmpty()) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    updateResources(base, it)
                } else {
                    updateResourcesLegacy(base, it)
                }
            } else {
                base
            }
        }
    }

    fun applyOverrideConfiguration(base: Context, overrideConfiguration: Configuration?): Configuration? {
        if (overrideConfiguration != null && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(base.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        return overrideConfiguration
    }

    private fun updateResources(base: Context, language: String): Context{
        val loc = Locale(language)
        Locale.setDefault(loc)
        val configuration = base.resources.configuration
        configuration.setLocale(loc)
        base.resources.updateConfiguration(configuration, base.resources.displayMetrics)
        return base.createConfigurationContext(configuration)
    }

    @Suppress("DEPRECATION")
    private fun updateResourcesLegacy(base: Context, language: String): Context{
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = base.resources.configuration
        configuration.locale = locale
        configuration.setLayoutDirection(locale)
        base.resources.updateConfiguration(configuration, base.resources.displayMetrics)
        return base
    }
}
