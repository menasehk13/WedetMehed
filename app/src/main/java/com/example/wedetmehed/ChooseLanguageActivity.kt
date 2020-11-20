package com.example.wedetmehed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class ChooseLanguageActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    val lists= listOf<String>("English","Amharic")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_choose_language)
        listView=findViewById(R.id.list_language)
        val arrayAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,lists)
        listView.adapter=arrayAdapter
        listView.onItemClickListener =
            AdapterView.OnItemClickListener { p0, p1, p2, p3 ->
                when(p2){
                    0->{
                            val prefManager=PrefManager(this)
                            prefManager.updateLanguage("en")
                            val intent= Intent(this,LoginActivity::class.java)
                            startActivity(intent)
                    }
                    1->{
                        val prefManager=PrefManager(this)
                        prefManager.updateLanguage("aa")
                        val intent= Intent(this,LoginActivity::class.java)
                        startActivity(intent)

                    }
                }
            }
    }

    private fun selectedLanguage(s: String) {

    }
}