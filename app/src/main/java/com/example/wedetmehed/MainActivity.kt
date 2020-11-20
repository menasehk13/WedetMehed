package com.example.wedetmehed

import android.content.pm.PackageManager
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentTransaction
import android.view.View
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class MainActivity :BaseActivity() {
    private  var CheckPerMISSION:Int=99
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
            checkpermission()
         val navigate=findViewById<MeowBottomNavigation>(R.id.navigation)
        navigate.add(MeowBottomNavigation.Model(1,R.drawable.ic_baseline_home_24))
        navigate.add(MeowBottomNavigation.Model(2,R.drawable.ic_baseline_book_24))
        navigate.add(MeowBottomNavigation.Model(3,R.drawable.ic_baseline_more_horiz_24))
        navigate.show(1,true)
        navigate.setOnShowListener {
            var fragment: Fragment? =null
            if (it.id==1){
                fragment=HomeFragment()
            }
            if (it.id==2){
                fragment=MyBookingFragment()
            }
            if (it.id==3){
                fragment=MoreFragment()
            }
            val ft=supportFragmentManager.beginTransaction()
            if (fragment != null) {
                ft.replace(R.id.frame,fragment)
            }
            ft.commit()
        }
    }
    private fun checkpermission(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),CheckPerMISSION)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            CheckPerMISSION->{
                if (grantResults.isEmpty()||grantResults[0]!= PackageManager.PERMISSION_GRANTED){

                }else{

                }
            }
        }
    }
}