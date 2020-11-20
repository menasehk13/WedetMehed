package com.example.wedetmehed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.rilixtech.widget.countrycodepicker.CountryCodePicker

class LoginPhoneActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login_phone)
        val prefManager=PrefManager(this)
        Log.d("language", prefManager.getlanguage().toString())
        val countrycode=findViewById<CountryCodePicker>(R.id.countrycode)
        val phonenum=findViewById<EditText>(R.id.phonelogin)
        val login=findViewById<ImageButton>(R.id.buttoncontinue)
        login.setOnClickListener(){
            val phone=phonenum.text
            val code=countrycode.selectedCountryCode
            if (phone.isEmpty()){
                phonenum.error="Please Add Your Phone"
                phonenum.isFocusable.and(true)
                return@setOnClickListener
            }
            val finalphonenum=code+phone
              Toast.makeText(this,finalphonenum,Toast.LENGTH_LONG).show()
            val intent=Intent(applicationContext,VerifyPhoneActivity::class.java)
            intent.putExtra("Phone",finalphonenum)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }
    }
}