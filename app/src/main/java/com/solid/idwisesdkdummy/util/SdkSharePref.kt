package com.solid.idwisesdkdummy.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.gson.Gson


class SdkSharePref (context: Context) {

    private var sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)


    fun setString(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
            apply()
        }
    }

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultString: String? = null) = sharedPreferences.getString(key, defaultString) ?: ""

    fun getBoolean(key: String, defaultBoolean: Boolean = false) = sharedPreferences.getBoolean(key, defaultBoolean)

    inline fun <reified T> saveObjectToSharedPreferences(sharedPreferences: SharedPreferences, key: String, obj: T) {
        sharedPreferences.edit{
            val gson = Gson()
            val json = gson.toJson(obj)
            putString(key, json)
            apply()
        }
    }

    inline fun <reified T> getObjectFromSharedPreferences(sharedPreferences: SharedPreferences, key: String): T? {
        val json = sharedPreferences.getString(key, null)
        val gson = Gson()
        return gson.fromJson(json, T::class.java)
    }

    fun clear(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}