package com.example.wedetmehed

import android.app.ActionBar
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SelectLanguage_Fragment : AppCompatActivity(){
    lateinit var listView: ListView
    val lists= listOf<String>("English","Amharic")
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_select_language_)
        listView=findViewById(R.id.listviewlanguage)
        supportActionBar?.title="Choose Language"
        val arrayAdapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,lists)
        listView.adapter=arrayAdapter
        listView.onItemClickListener = object:AdapterView.OnItemClickListener{
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0->PrefManager(applicationContext).updateLanguage("en")
                    1->{
                        PrefManager(applicationContext).updateLanguage("aa")
                        val intent=Intent(applicationContext,MainActivity::class.java)
                        startActivity(intent)
                    }
                }

            }

        }
    }




}