package com.example.wedetmehed

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock.sleep
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.FirebaseDatabase


class SplashActivity : AppCompatActivity() {
    private lateinit var connecting:TextView
    private lateinit var imageview: ImageView
    private lateinit var ConstraintLayout:ConstraintLayout
    private  var ms:Long=0
    private var splashtime:Long=4000
    private var splashactivity:Boolean=true
    private var paused:Boolean=false
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)
        connecting=findViewById(R.id.connecting)
        imageview=findViewById(R.id.splashimage)
        ConstraintLayout=findViewById(R.id.cl)
        val sharedPreferences:SharedPreferences=getSharedPreferences("firsttime",Context.MODE_PRIVATE)
        val firsttime:Boolean=sharedPreferences.getBoolean("isfirst",true)
        val animation:Animation=AnimationUtils.loadAnimation(this, R.anim.top_anim)
        imageview.animation=animation
        val thread=Thread(){
            run {
                try {
                    while (splashactivity && ms<splashtime){
                        if(!paused){
                            ms+=100
                            sleep(100)
                        }
                    }
                }catch (e: Exception){

                }
                finally {
                    if(!isOnline()){
                        val snackbar=Snackbar.make(ConstraintLayout,
                            "NO INTENET CONNECTION",
                            Snackbar.LENGTH_INDEFINITE)
                            .setAction("Retry", View.OnClickListener {
                                recreate()
                            })
                        snackbar.show()
                    } else {
                            val intent= Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }


                }
            }
        }
        thread.start()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline():Boolean{
        val connectionmanager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectionmanager.activeNetworkInfo!=null && connectionmanager.activeNetworkInfo!!.isConnectedOrConnecting
    }


}