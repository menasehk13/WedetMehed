package com.example.wedetmehed

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import android.view.View;
import androidx.core.util.Pair
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)

        val user=FirebaseAuth.getInstance().currentUser
        if (user!=null){
            val intent=Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val loginpage=findViewById<Button>(R.id.loginpage)
        val viewflipper=findViewById<ViewFlipper>(R.id.viewFlipper)
        var imageView=findViewById<ImageView>(R.id.logo)
        viewflipper.startFlipping()
        viewflipper.setInAnimation(applicationContext,android.R.anim.slide_in_left)
        viewflipper.setOutAnimation(applicationContext,android.R.anim.slide_out_right)
        loginpage.setOnClickListener(){
            val intent=Intent(this,LoginPhoneActivity::class.java)
            val p1=android.util.Pair.create<View,String>(imageView,"logo")
            val options=ActivityOptions.makeSceneTransitionAnimation(this,p1)
            startActivity(intent,options.toBundle())
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
            finish()
        }
    }
}