package com.dawarly.util

import android.content.Context
import android.content.SharedPreferences

object Preferences {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    const val PREFNAME = "CityClub"
    const val PREF_Settings = "KeySettings"
    const val PREF_Application_Locale = "ApplicationLocale"
    const val PREF_USER_ID = "UserId"
    const val PREF_USER_PHONE = "UserPhone"
    const val PREF_USER_EMAIL = "UserEmail"
    const val PREF_TOKEN = "Token"

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

    fun getUserId(): String {
        return sharedPreferences.getString(PREF_USER_ID, "")!!
    }

    fun saveUserId(response: String) {
        editor.putString(PREF_USER_ID, response)
        editor.commit()
    }


    fun getUserPhone(): String {
        return sharedPreferences.getString(PREF_USER_PHONE, "")!!
    }

    fun saveUserPhone(response: String) {
        editor.putString(PREF_USER_PHONE, response)
        editor.commit()
    }

    fun getUserEmail(): String {
        return sharedPreferences.getString(PREF_USER_EMAIL, "")!!
    }

    fun saveUserEmail(response: String) {
        editor.putString(PREF_USER_EMAIL, response)
        editor.commit()
    }

    fun getToken(): String {
        return sharedPreferences.getString(PREF_TOKEN, "")!!
    }

    fun saveToken(response: String) {
        editor.putString(PREF_TOKEN, response)
        editor.commit()
    }

    fun clearData() : String {
        return sharedPreferences.getString(PREF_TOKEN, "")!!
    }
}



