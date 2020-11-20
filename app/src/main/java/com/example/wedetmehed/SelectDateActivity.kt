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
    private  var piceof:Int?=null
    private lateinit var hotelname:TextView
    private lateinit var roomtype:TextView
    private lateinit var tax:TextView
    private lateinit var pricepernight:TextView
    private lateinit var totalprice:TextView
    private lateinit var roosize:TextView
    private lateinit var plussign:ImageButton
    private lateinit var minussign:ImageButton
    private lateinit var procedtopayement:Button
    private lateinit var ratingBar: RatingBar
    private lateinit var imageview: ImageView
    private lateinit var nightof:TextView
    private lateinit var sublimeDatePicker: SublimeDatePicker
    private lateinit var checkin:TextView
    private var datebetween by Delegates.notNull<Long>()
    private lateinit var checkout:TextView
    private  var roomsizeof: Int =1
    private var roomsizehotel:Int?=null
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_date)
        hotelname=findViewById(R.id.hotelname1)
        imageview=findViewById(R.id.selectedroomimage)
        roomtype=findViewById(R.id.roomtype1)
        tax=findViewById(R.id.taxofhotel)
        pricepernight=findViewById(R.id.pricepernight1)
        totalprice=findViewById(R.id.totalprice)
        roosize=findViewById(R.id.roomsizeselect)
        plussign=findViewById(R.id.plusroomsize)
        minussign=findViewById(R.id.minusroomsize)
        checkin=findViewById(R.id.checkin)
        checkout=findViewById(R.id.checkout)
        nightof=findViewById(R.id.numberofnights)
        roomsizeof=Integer.parseInt(roosize.text.toString())
        procedtopayement=findViewById(R.id.procedtopayemnt)
        //ratingBar=findViewById(R.id.ratingBarselected)
        hotelname.text=intent.getStringExtra("hotelnameof")
        roomtype.text=intent.getStringExtra("roomtype")
        val selectedhotel=intent.getStringExtra("hotelnameof")
        val selectedroom=intent.getStringExtra("roomtype")
       val dateFormat=SimpleDateFormat("E,MMM,dd")
        val long=System.currentTimeMillis()
        val long2=long +(1000 * 60 * 60 * 24);
        val date=dateFormat.format(long)
        val  date2=dateFormat.format(long2)
        val cal1=GregorianCalendar()
        val cal2=GregorianCalendar()
        cal1.time=dateFormat.parse(date)
        cal2.time=dateFormat.parse(date2)
        checkin.text=date
        checkout.text=date2
        datebetween=getdatebetween(cal1.time,cal2.time)
        nightof.text=datebetween.toString()+" "+"Night"
        Toast.makeText(applicationContext,"Firstvalue"+datebetween.toString(),Toast.LENGTH_LONG).show()
        tax.text="35br"
        checkin.setOnClickListener(){
            val builder=MaterialDatePicker.Builder.dateRangePicker()
            builder.setTitleText("Selecet Your Date")
            builder.setCalendarConstraints(limitrange().build())
            val materialDatePicker=builder.build()
            materialDatePicker.show(supportFragmentManager,builder.toString())
            materialDatePicker.addOnPositiveButtonClickListener {
              val selectedfirstdate=dateFormat.format(it.first)
                val lastdate=dateFormat.format(it.second)
                checkin.text=selectedfirstdate
                checkout.text=lastdate
              val calendar=GregorianCalendar()
                val caldendar2=GregorianCalendar()
                calendar.time=dateFormat.parse(selectedfirstdate)
                caldendar2.time=dateFormat.parse(lastdate)
                datebetween=getdatebetween(calendar.time,caldendar2.time)
                nightof.text=datebetween.toString()+" "+"Night"
                Toast.makeText(applicationContext,"Values is"+datebetween.toString(),Toast.LENGTH_LONG).show()

            }
        }
        val databaseReference=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelDetail").child(selectedhotel!!)
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val hoteldetail=snapshot.getValue(HotelClassDetail::class.java)
                //ratingBar.rating=hoteldetail!!.rating


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        val reference=databaseReference.child("RoomTpe").child(selectedroom!!)
        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val roomClassDetail=snapshot.getValue(RoomClassDetail::class.java)
                Picasso.get().load(roomClassDetail!!.roomimage).fit().into(imageview)
                 roomsizehotel=Integer.parseInt(roomClassDetail.roomsize)
                pricepernight.text=roomClassDetail.roomprice.toString()+""+"/PerNight"
                piceof=roomClassDetail.roomprice
                val t=piceof!!*1+35
                totalprice.text=t.toString()+"Br"
                plussign.setOnClickListener(){
                    if (roomsizeof!=roomsizehotel){
                        roomsizeof++
                        roosize.text=roomsizeof.toString()
                        val getroom=Integer.parseInt(roosize.text as String)
                        val finalprice=piceof!!*getroom+35
                        totalprice.text=finalprice.toString()+"Br"
                    }else{
                        Toast.makeText(applicationContext,"Max Room",Toast.LENGTH_LONG).show()
                    }
                }
                minussign.setOnClickListener(){
                    if (roomsizeof!=1){
                        roomsizeof--
                        roosize.text=roomsizeof.toString()
                        val getroom=Integer.parseInt(roosize.text as String)
                        val finalprice=piceof!!*getroom+35
                        totalprice.text=finalprice.toString()+"Br"
                    }else{
                        Toast.makeText(applicationContext,"Min Room",Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
          procedtopayement.setOnClickListener(){
              val inten2=Intent(applicationContext,ChoosePayemntMethod::class.java)
              val hoteloftheuser=intent.getStringExtra("hotelnameof")
              val roomtypeof=intent.getStringExtra("roomtype")
              val roomsizeselected=roosize.text.toString()
              val checkindate=checkin.text.toString()
              val checkoutdate=checkout.text.toString()
              val howmanynight=datebetween.toString()
              val totalprice=totalprice.text.toString()
              inten2.putExtra("hotelofuser",hoteloftheuser)
              inten2.putExtra("roomtypeof",roomtypeof)
              inten2.putExtra("roomsizeselected",roomsizeselected)
              inten2.putExtra("checkindate",checkindate)
              inten2.putExtra("checkoutdate",checkoutdate)
              inten2.putExtra("hownamynights",howmanynight)
              inten2.putExtra("priceTotal",totalprice)
              startActivity(inten2)
          }
    }
   private fun limitrange():CalendarConstraints.Builder{
       val calendarConstraints=CalendarConstraints.Builder()
       val startdate=System.currentTimeMillis()-1000
       val enddate=System.currentTimeMillis()+(1000*60*60*24*14)
       calendarConstraints.setStart(startdate)
       calendarConstraints.setEnd(enddate)
       calendarConstraints.setValidator(RangeValidator(startdate,enddate))
       return calendarConstraints
   }


class RangeValidator(private val mindate:Long,private val maxdate:Long):CalendarConstraints.DateValidator{
    constructor(parcel: Parcel):this(
        parcel.readLong(),
        parcel.readLong()
    )
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeLong(mindate)
        p0.writeLong(maxdate)
    }

    override fun isValid(date: Long): Boolean {
     return !(mindate>=date||maxdate<date)
    }

    companion object CREATOR : Creator<RangeValidator> {
        override fun createFromParcel(parcel: Parcel): RangeValidator {
            return RangeValidator(parcel)
        }

        override fun newArray(size: Int): Array<RangeValidator?> {
            return arrayOfNulls(size)
        }
    }

}

    companion object CREATOR : Creator<RangeValidator> {
        override fun createFromParcel(parcel: Parcel): RangeValidator {
            return RangeValidator(parcel)
        }

        override fun newArray(size: Int): Array<RangeValidator?> {
            return arrayOfNulls(size)
        }
    }
            fun getdatebetween( dateone:Date,  datetwo:Date): Long {
             return  ((datetwo.time - dateone.time) / (1000 * 60 * 60 * 24))
            }
   }


