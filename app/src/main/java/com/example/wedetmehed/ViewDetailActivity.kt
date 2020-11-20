package com.example.wedetmehed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlin.math.log

class ViewDetailActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var hotelname:TextView
    private lateinit var rating:RatingBar
    private lateinit var imageview:ImageView
    private lateinit var gMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_view_detail)
         hotelname=findViewById(R.id.detailviewhotelname)
        imageview=findViewById(R.id.displayviewimage)
        rating=findViewById(R.id.detailviewrating)
        val hotel= intent.getStringExtra("hotelvalue")
        val refer=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelDetail").child(hotel!!)
        refer.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val hotelClassDetail=snapshot.getValue(HotelClassDetail::class.java)
                rating.rating=hotelClassDetail!!.rating
                Picasso.get().load(hotelClassDetail.imageurl).fit().into(imageview)
                hotelname.text=hotelClassDetail.hotelname
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        val mapfragmet=supportFragmentManager.findFragmentById(R.id.fragment_map) as SupportMapFragment
        mapfragmet.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap=p0
        val hotellocation=intent.getStringExtra("hotelvalue")
        val referlocation=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelLocation")
        val geoFire=GeoFire(referlocation)
        geoFire.getLocation(hotellocation, object : LocationCallback {
            override fun onLocationResult(key: String?, location: GeoLocation?) {
                if (location!=null) {
                    val latlang = LatLng(location.latitude, location.longitude)
                    Log.d("Location",
                        "onLocationResult: " + location.latitude + "/" + location.longitude)
                    gMap.addMarker(MarkerOptions().position(latlang)
                        .icon(BitmapDescriptorFactory.defaultMarker()))
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlang, 12f))
                }
            }

            override fun onCancelled(databaseError: DatabaseError?) {

            }

        })
    }
}