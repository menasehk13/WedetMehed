package com.example.wedetmehed

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.IntegerRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.parser.IntegerParser
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.firebase.geofire.GeoQueryEventListener
import com.firebase.geofire.util.GeoUtils
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.maps.android.ui.IconGenerator
import com.squareup.picasso.Picasso
import java.text.DecimalFormat
import kotlin.math.log

class MapActivity :AppCompatActivity(), OnMapReadyCallback,LocationListener, View.OnClickListener {
    private lateinit var googleMap: GoogleMap
    private lateinit var location: Location
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var searchnearby:ImageButton
    private lateinit var mylocation:FloatingActionButton
    private lateinit var cardview:CardView
    private lateinit var Hotelname:TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var imageView: ImageView
    private lateinit var distance:TextView
    private lateinit var searching:TextView
    private lateinit var nearbyanimation:LottieAnimationView
    private lateinit var price:TextView
    private lateinit var openhotel:Button
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var userlocation:GeoLocation
    private lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        supportActionBar?.hide()
        val mapFragment=supportFragmentManager.findFragmentById(R.id.maps)as SupportMapFragment
        mapFragment.getMapAsync(this)
        constraintLayout=findViewById(R.id.bottomsheet)
        bottomSheetBehavior= BottomSheetBehavior.from<ConstraintLayout>(constraintLayout)
        cardview=findViewById(R.id.cardview1)
        Hotelname=findViewById(R.id.hotelnameselected)
        ratingBar=findViewById(R.id.ratingBarselected)
        distance=findViewById(R.id.distance)
        imageView=findViewById(R.id.hotelimageof)
        openhotel=findViewById(R.id.openhotel)
        price=findViewById(R.id.pricepernight)
        mylocation=findViewById(R.id.mylocation)
        bottomSheetBehavior.state=BottomSheetBehavior.STATE_EXPANDED
        searchnearby=findViewById(R.id.searchnearby)
        nearbyanimation=findViewById(R.id.animationnearby)
        searchnearby.setOnClickListener(this)
        searching=findViewById(R.id.searching)
        mylocation.setOnClickListener(){
            onLocationChanged(location)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap=p0
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext,R.raw.mapstyle))
        locationManager=getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        googleMap.isMyLocationEnabled=true
        googleMap.uiSettings.isMyLocationButtonEnabled=false
        location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        if (location!=null){
            onLocationChanged(location)
        }
       locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0f,this)
        googleMap.setInfoWindowAdapter(CustominfoWindow(applicationContext))

    }

    override fun onLocationChanged(p0: Location) {
        val lat=p0.latitude
        val lng=p0.longitude
        val latLng=LatLng(lat,lng)
        userlocation= GeoLocation(lat,lng)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f))
    }

    override fun onClick(p0: View?) {
        bottomSheetBehavior.state=BottomSheetBehavior.STATE_HIDDEN
        searching.visibility=View.VISIBLE
        nearbyanimation.visibility=View.VISIBLE
        val databaseReference=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelLocation")
        val geoFire=GeoFire(databaseReference)
        val geoQuery=geoFire.queryAtLocation(userlocation,5.0)
        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onKeyEntered(key: String, location1: GeoLocation) {
                val reference = FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelDetail")
                        .child(key)

                reference.addValueEventListener(object : ValueEventListener {
                    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onDataChange(snapshot: DataSnapshot) {
                        nearbyanimation.visibility=View.INVISIBLE
                        searching.visibility=View.INVISIBLE
                        val hotelClassDetail = snapshot.getValue(HotelClassDetail::class.java)
                        val latLng2 = LatLng(location1.latitude, location1.longitude)

                        val markerOptions =
                            MarkerOptions().icon(bitmapget(applicationContext,R.drawable.iconhotel))
                                .position(latLng2)
                        var marker = googleMap.addMarker(markerOptions)
                        marker.tag = hotelClassDetail
                        marker.position
                        marker.tag
                        marker.hideInfoWindow()
                        googleMap.setOnMarkerClickListener {
                            var hotelname:HotelClassDetail= HotelClassDetail()
                            hotelname= it.tag as HotelClassDetail
                            if (marker!=null)
                                marker.setIcon(bitmapget(applicationContext,R.drawable.iconhotel))
                                it.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                cardview.visibility=View.VISIBLE
                                Hotelname.text=hotelname.hotelname
                                Picasso.get().load(hotelname.imageurl).fit().into(imageView)
                                val geoLocation=GeoLocation(it.position.latitude,it.position.longitude)
                                val double=GeoUtils.distance(userlocation,geoLocation)/1000
                                val decimalforma=DecimalFormat("#.##")
                                distance.text= decimalforma.format(double)+" "+"Km"+" "+"Away"
                                ratingBar.rating=hotelname.rating
                                price.text=hotelname.price.toString()+"br"
                                openhotel.setOnClickListener(){
                                    val intent= Intent(applicationContext,SelectRoomActivity::class.java)
                                    val userselectedhotelname=Hotelname.text.toString()
                                    intent.putExtra("hotel",userselectedhotelname)
                                    startActivity(intent)
                                }
                                marker=it

                              true
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }

            override fun onKeyExited(key: String?) {
            }

            override fun onKeyMoved(key: String?, location: GeoLocation?) {
            }

            override fun onGeoQueryReady() {
            }

            override fun onGeoQueryError(error: DatabaseError?) {
            }
        })
    }

    private fun bitmapget(applicationContext: Context, mapmarker: Int): BitmapDescriptor {
      val drawable: Drawable? =ContextCompat.getDrawable(applicationContext,mapmarker)
        drawable?.setBounds(0,0,drawable.intrinsicWidth,drawable.intrinsicHeight)
        val bitmap=Bitmap.createBitmap(drawable?.intrinsicWidth!!,drawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}