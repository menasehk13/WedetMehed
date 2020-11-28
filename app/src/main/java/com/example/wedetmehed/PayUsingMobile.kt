package com.example.wedetmehed


import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class PayUsingMobile:AppCompatActivity() {
    lateinit var confirmpayment: Button
    var SMS_textview: TextView? = null
     lateinit var txnid: EditText
    private  var stringtxnid:String?=null
    private var hotelname: String? = null
    private var roomtype: String? = null
    private var roomsize: String? = null
    private var txnidof:String?=null
    private var nights: String? = null
    private var checkindate: String? = null
    private var checkoutdate: String? = null
    private var totalprice: String? = null
    private var hotelimage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_viewfor_mobilepayment)
        totalprice=intent.getStringExtra("priceTotal")
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:*847*1*0913637519*$totalprice" + Uri.encode("#"))
        startActivity(callIntent)
        val value="trlrtsrsr."
        val val2=value.replace(".","")
        Log.d("ssdas", "onCreate: "+val2)
        confirmpayment=findViewById(R.id.payfinal)
        txnid=findViewById(R.id.txnid)
        txnid.inputType=InputType.TYPE_NULL
        txnid.setTextIsSelectable(false)
        txnid.setOnKeyListener(object : View.OnKeyListener{
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                return true
            }

        })
        SMS_textview=findViewById(R.id.showtxnid)
        val otpReceiver=OTP_Receiver()
        otpReceiver.setEditText(txnid)
        confirmpayment.setOnClickListener {
           stringtxnid=txnid.text.toString()
            if (stringtxnid!!.isEmpty()|| stringtxnid!!.length<10)
                txnid.error="Txn ID is Not Correct"
            else
                checkcodeandvalidate(stringtxnid!!)
        }

    }

    private fun checkcodeandvalidate(stringtxnid: String) {
        hotelname=intent.getStringExtra("hotelofuser")
        roomtype=intent.getStringExtra("roomtypeof")
        roomsize=intent.getStringExtra("roomsizeselected")
        nights=intent.getStringExtra("hownamynights")
        checkindate=intent.getStringExtra("checkindate")
        checkoutdate=intent.getStringExtra("checkoutdate")
        totalprice=intent.getStringExtra("priceTotal")
        hotelimage=intent.getStringExtra("imageof")
        val databaseReference= FirebaseDatabase.getInstance().reference.child("transactionid")
        val query=databaseReference.orderByChild("transaction").equalTo(stringtxnid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Toast.makeText(applicationContext, "BAD TXNID", Toast.LENGTH_LONG).show()
                    return
                } else {
                    val auth = FirebaseAuth.getInstance()
                    val stringuid = auth.currentUser?.uid
                    val documentReference =
                        FirebaseFirestore.getInstance().collection("Users").document(
                            stringuid!!
                        )
                    documentReference.get().addOnCompleteListener() {
                        val documentSnapshot = it.result
                        if (documentSnapshot.exists()) {
                            val username: String = documentSnapshot.get("FullName").toString()
                            val phone: String = documentSnapshot.get("PhoneNumber").toString()
                            val ordernumber = createrandomordernumber(6)
                            val bookingId = BookingIdGenerator(5)
                            val refernce = FirebaseDatabase.getInstance().reference.child("Hotels").child(
                                    "HotelDetail"
                                ).child(hotelname!!).child("RESERVED")
                            val myreservation = FirebaseDatabase.getInstance().reference.child("Hotels").child("Myreservation").child(stringuid)
                            val transactionid =
                                FirebaseDatabase.getInstance().reference.child("transactionid")
                            val reservationDetail = ReservationDetail(
                                hotelname!!,
                                roomsize!!,
                                roomtype!!,
                                nights!!,
                                totalprice,
                                checkindate!!,
                                checkoutdate!!,
                                stringtxnid,
                                username,
                                phone,
                                ordernumber,
                                bookingId,
                                hotelimage!!
                            )
                            myreservation.push().setValue(reservationDetail)
                            refernce.child(username).push().setValue(reservationDetail)
                            val id = transactionid.push().key
                            val transactionDetail = TransactionDetail(stringtxnid)
                            transactionid.child(id!!).setValue(transactionDetail)
                            val intent = Intent(applicationContext, FinalPaymentConfirm::class.java)
                            intent.putExtra("ordernumber", ordernumber)
                            intent.putExtra("finalamount", totalprice)
                            intent.putExtra("hotelname", hotelname)
                            intent.putExtra("roomtypeof", roomtype)
                            intent.putExtra("howmayrooms", roomsize)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }

            @RequiresApi(Build.VERSION_CODES.N)
        private fun createrandomordernumber(i: Int):String{
            var j=i
            var character:String="1234567890"
            var builder=StringBuilder()
            while (j>0){
                val random= Random()
                builder.append(character[random.nextInt(character.length)])
                j--
            }
            return builder.toString()
         }
       private fun BookingIdGenerator(i: Int):String {
           var j = i
           val character = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"
           val builder = StringBuilder()
           while (j > 0) {
               val random = Random()
               builder.append(character[random.nextInt(character.length)])
               j--
           }
           return builder.toString()
       }

    fun recivedSms(message: String) {
        Log.d("valueof", "sms is sms : "+message)
    }

}




