package com.example.wedetmehed

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.protobuf.StringValue
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class CheckAvalibility:AppCompatActivity(), View.OnClickListener {
    private lateinit var hotelimage:ImageView
    private lateinit var hotelname:TextView
    private lateinit var checkin:TextView
    private lateinit var checkout:TextView
    private lateinit var roomImage:ImageView
    private lateinit var roomtype:TextView
    private lateinit var roomsize:TextView
    private lateinit var plusroom:ImageButton
    private lateinit var minusroom:ImageButton
    private lateinit var checkAvalibility:Button
    private lateinit var radioGroup: RadioGroup
    private var priceofhotel:String?=null
    private var datebetween by Delegates.notNull<Long>()
    private  var roomsizeof: Int =1
    private var roomsizehotel:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_check_avalibilty_of_room)
        init()
        val hotelselectedname=intent.getStringExtra("hotel")
        hotelname.text=hotelselectedname
        val database=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelDetail").child(hotelselectedname!!)
        database.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val hotelClassDetail=snapshot.getValue(HotelClassDetail::class.java)
                Picasso.get().load(hotelClassDetail?.imageurl.toString()).fit().into(hotelimage)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        /*val databse2=database.child("RoomTpe").child("SingleRoom")
        databse2.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val roomClassDetail=snapshot.getValue(RoomClassDetail::class.java)
                roomtype.text=roomClassDetail!!.roomtype
                Picasso.get().load(roomClassDetail.roomimage).fit().into(roomImage)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

         */
        val dateFormat= SimpleDateFormat("E,MMM,dd")
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
            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val database2=database.child("RoomTpe")
                if (checkedId == R.id.singleroom) {
                    roomsize.text="1"
                    database2.child("SingleRoom").addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val roomClassDetail=snapshot.getValue(RoomClassDetail::class.java)
                            roomtype.text=roomClassDetail!!.roomtype
                            Picasso.get().load(roomClassDetail.roomimage).fit().into(roomImage)
                            roomsizehotel=Integer.parseInt(roomClassDetail.roomsize)
                            priceofhotel=roomClassDetail.roomprice.toString()
                            plusroom.setOnClickListener {
                                if (roomsizeof!=roomsizehotel){
                                    roomsizeof++
                                    roomsize.text=roomsizeof.toString()
                                }else{
                                    Toast.makeText(applicationContext,"Max Room",Toast.LENGTH_LONG).show()
                                }
                            }
                            minusroom.setOnClickListener {
                                if (roomsizeof!=1){
                                    roomsizeof--
                                    roomsize.text=roomsizeof.toString()
                                }else{
                                    Toast.makeText(applicationContext,"Min Room",Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                } else if (checkedId == R.id.doubleroom) {
                    roomsize.text="1"
                    database2.child("DoubleRoom").addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val roomClassDetail=snapshot.getValue(RoomClassDetail::class.java)
                            roomtype.text=roomClassDetail!!.roomtype
                            Picasso.get().load(roomClassDetail.roomimage).fit().into(roomImage)
                            roomsizehotel=Integer.parseInt(roomClassDetail.roomsize)
                            priceofhotel=roomClassDetail.roomprice.toString()
                            plusroom.setOnClickListener {
                                if (roomsizeof!=roomsizehotel){
                                    roomsizeof++
                                    roomsize.text=roomsizeof.toString()
                                }else{
                                    Toast.makeText(applicationContext,"Max Room",Toast.LENGTH_LONG).show()
                                }
                            }
                            minusroom.setOnClickListener {
                                if (roomsizeof!=1){
                                    roomsizeof--
                                    roomsize.text=roomsizeof.toString()
                                }else{
                                    Toast.makeText(applicationContext,"Min Room",Toast.LENGTH_LONG).show()
                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                } else if (checkedId == R.id.suitroom) {
                    roomsize.text="1"
                    database2.child("SuitRoom").addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val roomClassDetail=snapshot.getValue(RoomClassDetail::class.java)
                            roomtype.text=roomClassDetail!!.roomtype
                            Picasso.get().load(roomClassDetail.roomimage).fit().into(roomImage)
                            roomsizehotel=Integer.parseInt(roomClassDetail.roomsize)
                            priceofhotel=roomClassDetail.roomprice.toString()
                            plusroom.setOnClickListener {
                                if (roomsizeof!=roomsizehotel){
                                    roomsizeof++
                                    roomsize.text=roomsizeof.toString()
                                }else{
                                    Toast.makeText(applicationContext,"Max Room",Toast.LENGTH_LONG).show()
                                }
                            }
                            minusroom.setOnClickListener {
                                if (roomsizeof!=1){
                                    roomsizeof--
                                    roomsize.text=roomsizeof.toString()
                                }else{
                                    Toast.makeText(applicationContext,"Min Room",Toast.LENGTH_LONG).show()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
                }
            }

        checkin.setOnClickListener(){
            val builder= MaterialDatePicker.Builder.dateRangePicker()
            builder.setTitleText("Selecet Your Date")
            builder.setCalendarConstraints(limitrange().build())
            val materialDatePicker=builder.build()
            materialDatePicker.show(supportFragmentManager,builder.toString())
            materialDatePicker.addOnPositiveButtonClickListener {
                val selectedfirstdate=dateFormat.format(it.first)
                val lastdate=dateFormat.format(it.second)
                checkin.text=selectedfirstdate
                checkout.text=lastdate
                val calendar= GregorianCalendar()
                val caldendar2= GregorianCalendar()
                calendar.time=dateFormat.parse(selectedfirstdate)
                caldendar2.time=dateFormat.parse(lastdate)
                datebetween=getdatebetween(calendar.time,caldendar2.time)
               //nightof.text=datebetween.toString()+" "+"Night"
                Toast.makeText(applicationContext,"Values is"+datebetween.toString(),Toast.LENGTH_LONG).show()

            }
        }
        checkAvalibility.setOnClickListener(this)
    }

    private fun init() {
        hotelname=findViewById(R.id.hotelcheckname)
        hotelimage=findViewById(R.id.hotelcheckimage)
        checkin=findViewById(R.id.displaycheckin)
        checkout=findViewById(R.id.displaycheckout)
        roomImage=findViewById(R.id.displaycheckroomimage)
        roomtype=findViewById(R.id.displaycheckroomtype)
        roomsize=findViewById(R.id.roomsizeselect)
        plusroom=findViewById(R.id.plusroomsize)
        minusroom=findViewById(R.id.minusroomsize)
        checkAvalibility=findViewById(R.id.cheking)
        radioGroup=findViewById(R.id.radiorooms)
    }

    override fun onClick(v: View?) {
        if (radioGroup.checkedRadioButtonId==-1){
            Toast.makeText(this,"please select A Room ",Toast.LENGTH_LONG).show()

        }
        val datecheckin=checkin.text.toString()
        val roomsizeselected=roomsize.text.toString()
       val userroom=roomtype.text.toString()
        val hotelselectedname=hotelname.text.toString()
        when(radioGroup.checkedRadioButtonId){
          R.id.singleroom->{
           checkAvalibilityofroom(userroom,datecheckin,roomsizeselected,hotelselectedname)

          }
          R.id.doubleroom->{
              checkAvalibilityofroom(userroom,datecheckin,roomsizeselected,hotelselectedname)

          }
          R.id.suitroom->{
              checkAvalibilityofroom(userroom,datecheckin,roomsizeselected,hotelselectedname)
          }
      }
    }

    private fun checkAvalibilityofroom(
        userroom: String,
        datecheckin: String,
        roomsizeselected: String,
        hotelselectedname: String
    ) {
        Log.d("ROOMSELECTED", userroom)
        val datecheckout=checkout.text.toString()
        val night=datebetween.toString()
        val intent= Intent(applicationContext,SelectDateActivity::class.java)
        intent.putExtra("roomtype",userroom)
        intent.putExtra("hotelnameof",hotelselectedname)
        intent.putExtra("roomsizeselected",roomsizeselected)
        intent.putExtra("datecheckin",datecheckin)
        intent.putExtra("datecheckout",datecheckout)
        intent.putExtra("howmanynights",night)
        intent.putExtra("priceofroom",priceofhotel)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP;
        startActivity(intent)
        finish()
    }

    private fun limitrange(): CalendarConstraints.Builder{
        val calendarConstraints= CalendarConstraints.Builder()
        val startdate=System.currentTimeMillis()-1000
        val enddate=System.currentTimeMillis()+(1000*60*60*24*14)
        calendarConstraints.setStart(startdate)
        calendarConstraints.setEnd(enddate)
        calendarConstraints.setValidator(RangeValidator(startdate,enddate))
        return calendarConstraints
    }


    class RangeValidator(private val mindate:Long,private val maxdate:Long): CalendarConstraints.DateValidator{
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

        companion object CREATOR : Parcelable.Creator<RangeValidator> {
            override fun createFromParcel(parcel: Parcel): RangeValidator {
                return RangeValidator(parcel)
            }

            override fun newArray(size: Int): Array<RangeValidator?> {
                return arrayOfNulls(size)
            }
        }

    }

    companion object CREATOR : Parcelable.Creator<RangeValidator> {
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


