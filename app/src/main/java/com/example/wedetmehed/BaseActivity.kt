package com.example.wedetmehed

import android.content.Context
import android.content.ContextWrapper
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import java.util.*

open class BaseActivity :AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val sharedPreferences=newBase.getSharedPreferences("Language",Context.MODE_PRIVATE)
        val languageselected:String= sharedPreferences.getString("language","en").toString()
        val language=languageselected
        val localeswitchto: Locale = Locale(language)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeswitchto)
        super.attachBaseContext(localeUpdatedContext)
    }
}