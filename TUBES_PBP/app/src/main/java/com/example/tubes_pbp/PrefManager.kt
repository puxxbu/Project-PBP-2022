package com.example.tubes_pbp

import android.content.Context
import android.content.SharedPreferences
import com.example.tubes_pbp.entity.room.Users
import com.google.gson.Gson

class PrefManager(var context: Context?) {

    // Shared pref mode
    val PRIVATE_MODE = 0

    // Sharedpref file name
    private val PREF_NAME = "SharedPreferences"

    private val IS_LOGIN = "is_login"
    private val IS_INSTALL = "is_install"

    var pref: SharedPreferences? = context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    var editor: SharedPreferences.Editor? = pref?.edit()

    fun setLoggin(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun setUser(user: Users){
        var json = Gson().toJson(user)
        editor?.putString("user", json)
        editor?.commit()
    }

    fun setUserID(id: Int){
        editor?.putInt("userID", id)
        editor?.commit()
    }

    fun getUserID(): Int? {
         return pref?.getInt("userID",-1)

    }

    fun setInstall(isInstall: Boolean){
        editor?.putBoolean(IS_INSTALL,isInstall)
        editor?.commit()
    }

    fun isInstalled(): Boolean?{
        return pref?.getBoolean(IS_INSTALL,false)
    }

    fun getUser(): Users? {
        var json = Gson().fromJson(pref?.getString("user",""), Users::class.java)
        return json
    }

    fun setUsername(username: String?) {
        editor?.putString("username", username)
        editor?.commit()
    }

    fun isLogin(): Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun getUsername(): String? {
        return pref?.getString("username", "")
    }

    fun removeData() {
        editor?.clear()
        editor?.commit()
    }
}