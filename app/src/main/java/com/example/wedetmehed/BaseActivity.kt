package com.example.wedetmehed

import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatActivity
import java.util.*

open class BaseActivity :AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val language="aa"
        val localeswitchto: Locale = Locale(language)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeswitchto)
        super.attachBaseContext(localeUpdatedContext)
    }
}