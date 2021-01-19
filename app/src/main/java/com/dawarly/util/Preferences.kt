package com.dawarly.util

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    const val PREFNAME = "Dawarly"
    const val PREF_Settings = "KeySettings"
    const val PREF_Application_Locale = "ApplicationLocale"

    fun initPreference(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveSettings(response: String) {
        editor.putString(PREF_Settings, response)
        editor.commit()
    }

    fun getSettings(): String? {
        return sharedPreferences.getString(PREF_Settings, "")
    }

    fun saveApplicationLocale(response: String) {
        editor.putString(PREF_Application_Locale, response)
        editor.commit()
    }

    fun getApplicationLocale(): String {
        return sharedPreferences.getString(PREF_Application_Locale, "")!!
    }
}


