package com.example.wedetmehed

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PrefManager(var  context:Context) {

    private var saveusername:String="SaveUsername"
    private var savelanguage:String="Language"
        fun saveUserName(username:String){
          val sharedPreferences=context.getSharedPreferences(saveusername,Context.MODE_PRIVATE)
            val edit:SharedPreferences.Editor= sharedPreferences.edit()
            edit.putString("Username",username)
            edit.apply()
        }
    fun getusername(): String? {
        val sharedPreferences=context.getSharedPreferences(saveusername,Context.MODE_PRIVATE)
        return sharedPreferences.getString("Username","username")
    }

    @SuppressLint("CommitPrefEdits")
    fun updateLanguage(s: String) {
        val sharedPreferences=context.getSharedPreferences(savelanguage,Context.MODE_PRIVATE)
        val edit:SharedPreferences.Editor=sharedPreferences.edit()
        edit.putString("Language",s)
        edit.apply()
    }
    fun getlanguage(): String? {
        val sharedPreferences=context.getSharedPreferences(savelanguage,Context.MODE_PRIVATE)
        return sharedPreferences.getString("Language","en")
    }
}