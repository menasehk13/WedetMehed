package com.example.wedetmehed

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.widget.*
import com.airbnb.lottie.parser.IntegerParser
import com.appeaser.sublimepickerlibrary.datepicker.SublimeDatePicker
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class SelectDateActivity : AppCompatActivity() {
    private var piceof: Int? = null
    private lateinit var hotelname: TextView
    private lateinit var roomtype: TextView
    private lateinit var tax: TextView
    private lateinit var pricepernight: TextView
    private lateinit var totalprice: TextView
    private lateinit var roosize: TextView
    private lateinit var plussign: ImageButton
    private lateinit var minussign: ImageButton
    private lateinit var procedtopayement: Button
    private lateinit var ratingBar: RatingBar
    private lateinit var imageview: ImageView
    private lateinit var nightof: TextView
    private lateinit var sublimeDatePicker: SublimeDatePicker
    private lateinit var checkin: TextView
    private var datebetween by Delegates.notNull<Long>()
    private lateinit var checkout: TextView
    private var roomsizeof: Int = 1
    private var roomsizehotel: Int? = null
    private  var imageof:String?=null

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_date)
        hotelname = findViewById(R.id.hotelname1)
        imageview = findViewById(R.id.selectedroomimage)
        roomtype = findViewById(R.id.roomtype1)
        tax = findViewById(R.id.taxofhotel)
        pricepernight = findViewById(R.id.pricepernight1)
        totalprice = findViewById(R.id.totalprice)
        nightof = findViewById(R.id.numberofnights)
        roosize=findViewById(R.id.roomsizeselect)
        roomsizeof = Integer.parseInt(roosize.text.toString())
        procedtopayement = findViewById(R.id.procedtopayemnt)
        //ratingBar=findViewById(R.id.ratingBarselected)
        hotelname.text = intent.getStringExtra("hotelnameof")
        roomtype.text = intent.getStringExtra("roomtype")
        val selectedhotel = intent.getStringExtra("hotelnameof")
        //val selectedroom = intent.getStringExtra("roomtype")
         val database=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelDetail").child(selectedhotel!!)
        database.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val hotelClassDetail=snapshot.getValue(HotelClassDetail::class.java)
                Picasso.get().load(hotelClassDetail!!.imageurl).fit().into(imageview)
                 imageof=hotelClassDetail.imageurl
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
            tax.text="35"
        val taxpayed=Integer.parseInt(tax.text.toString())
            roosize.text=intent.getStringExtra("roomsizeselected")
        val roomsizepayed=Integer.parseInt(roosize.text.toString())
        nightof.text=intent.getStringExtra("howmanynights")
        val nightpayed=Integer.parseInt(nightof.text.toString())
            roomtype.text=intent.getStringExtra("roomtype")
            val totalpricepayed=intent.getStringExtra("priceofroom")
            pricepernight.text=totalpricepayed
            val totalpricewillbepayed=Integer.parseInt(totalpricepayed)
            val finalpayemnt=roomsizepayed*nightpayed*totalpricewillbepayed
            val total=finalpayemnt+taxpayed
             totalprice.text=total.toString()
        procedtopayement.setOnClickListener() {
            val inten2 = Intent(applicationContext, ChoosePayemntMethod::class.java)
            val hoteloftheuser = intent.getStringExtra("hotelnameof")
            val roomtypeof = intent.getStringExtra("roomtype")
            val roomsizeselected = intent.getStringExtra("roomsizeselected")
            val checkindate = intent.getStringExtra("datecheckin")
            val checkoutdate =intent.getStringExtra("datecheckout")
            val howmanynight = intent.getStringExtra("howmanynights")
            val totalprice = totalprice.text.toString()
            inten2.putExtra("hotelofuser", hoteloftheuser)
            inten2.putExtra("roomtypeof", roomtypeof)
            inten2.putExtra("roomsizeselected", roomsizeselected)
            inten2.putExtra("checkindate", checkindate)
            inten2.putExtra("checkoutdate", checkoutdate)
            inten2.putExtra("hownamynights", howmanynight)
            inten2.putExtra("priceTotal", totalprice)
            inten2.putExtra("imageof",imageof)
            inten2.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP;
            startActivity(inten2)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onPause() {
        super.onPause()
        finish()
    }

}