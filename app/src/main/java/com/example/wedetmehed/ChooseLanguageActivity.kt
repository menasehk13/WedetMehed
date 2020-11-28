package com.example.wedetmehed

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class ChooseLanguageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_choose_language)
        val button=findViewById<Button>(R.id.next)
        val radioGroup=findViewById<RadioGroup>(R.id.radiogrouppay)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radiobutton=radioGroup.findViewById<RadioButton>(checkedId)
            if (radiobutton!=null) {
                when (checkedId) {
                    R.id.english -> {
                        button.isClickable = true
                        val sharedPreferences=getSharedPreferences("Language", Context.MODE_PRIVATE)
                        val editor=sharedPreferences.edit();
                        editor.putString("language","en")
                        editor.apply()

                    }
                    R.id.amharic -> {
                        button.isClickable = true
                        val sharedPreferences=getSharedPreferences("Language", Context.MODE_PRIVATE)
                        val editor=sharedPreferences.edit();
                        editor.putString("language","aa")
                        editor.apply()
                    }
                }
            }

        }
        button.setOnClickListener(){
            val intent=Intent(applicationContext,LoginActivity::class.java)
            startActivity(intent)
        }
    }
   class SmSBroadCasting:BroadcastReceiver(){
       override fun onReceive(context: Context?, intent: Intent?) {

       }

   }
}