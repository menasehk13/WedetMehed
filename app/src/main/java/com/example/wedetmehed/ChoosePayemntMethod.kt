package com.example.wedetmehed

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ChoosePayemntMethod : AppCompatActivity() {
    private lateinit var payinhotel:CheckBox
    private lateinit var payusingmobile:CheckBox
    private lateinit var payusingpaypal:CheckBox
    private lateinit var proccedtopayment:Button
    private  var  hotelname:String? = null
    private var roomtype:String?=null
    private var roomsize:String?=null
    private var nights:String?=null
    private var checkindate:String?=null
    private var checkoutdate:String?=null
    private var totalprice:String?=null
    private var hotelimage:String?=null
    private  var TAG="Value"
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_payemnt_method)

         payinhotel=findViewById(R.id.payusingcash)
        payusingmobile=findViewById(R.id.payusingmobile)
        payusingpaypal=findViewById(R.id.payusingpaypal)
        proccedtopayment=findViewById(R.id.confirmpayment)
        hotelname=intent.getStringExtra("hotelofuser")
         roomtype=intent.getStringExtra("roomtypeof")
         roomsize=intent.getStringExtra("roomsizeselected")
         nights=intent.getStringExtra("hownamynights")
         checkindate=intent.getStringExtra("checkindate")
         checkoutdate=intent.getStringExtra("checkoutdate")
         totalprice=intent.getStringExtra("priceTotal")
        val databaseReference=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelDetail").child(hotelname!!)
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val hotelClassDetail=snapshot.getValue(HotelClassDetail::class.java)
                hotelimage=hotelClassDetail?.imageurl.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
     proccedtopayment.setOnClickListener(){
         if (payusingmobile.isChecked){
             val alertDialogbulder=AlertDialog.Builder(ContextThemeWrapper(this,android.R.style.ThemeOverlay_Material_Dialog_Alert))
             val viewGroup=findViewById<ViewGroup>(android.R.id.content)
             val view=LayoutInflater.from(it.context).inflate(R.layout.custom_viewfor_mobilepayment,viewGroup,false)
             val buttonproced=view.findViewById<Button>(R.id.payfinal)
             val txnid=view.findViewById<EditText>(R.id.txnid)
             buttonproced.setOnClickListener(){
                 val stringtxnid=txnid.text.toString()
                 if (stringtxnid.isEmpty()||stringtxnid.length<10)
                     txnid.error="Txn ID is Not Correct"
                 else
                     checkcodeandvalidate(stringtxnid)
             }
             alertDialogbulder.setView(view)
             val alertdialogview=alertDialogbulder.create()
             alertdialogview.show()
         }
      }
    }

    private fun checkcodeandvalidate(stringtxnid: String) {
        val databaseReference=FirebaseDatabase.getInstance().reference.child("transactionid")
        val query=databaseReference.orderByChild("transaction").equalTo(stringtxnid)
        query.addListenerForSingleValueEvent(object:ValueEventListener{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    Toast.makeText(applicationContext,"BAD TXNID",Toast.LENGTH_LONG).show()
                    return
                }else{
                    val auth=FirebaseAuth.getInstance()
                    val stringuid=auth.currentUser?.uid
                    val documentReference=FirebaseFirestore.getInstance().collection("Users").document(stringuid!!)
                    documentReference.get().addOnCompleteListener(){
                        val documentSnapshot=it.result
                        if (documentSnapshot.exists()){
                            val username:String=documentSnapshot.get("FullName").toString()
                            val phone:String=documentSnapshot.get("PhoneNumber").toString()
                            val ordernumber=createrandomordernumber(6)
                            val bookingId=BookingIdGenerator(5)
                            val refernce=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelDetail").child(hotelname!!).child("RESERVED")
                            val myreservation=FirebaseDatabase.getInstance().reference.child("Hotels").child("Myreservation").child(stringuid)
                            val transactionid=FirebaseDatabase.getInstance().reference.child("transactionid")
                            val reservationDetail=ReservationDetail(hotelname!!,roomsize!!,roomtype!!,nights!!,totalprice,checkindate!!,checkoutdate!!,stringtxnid,username,phone,ordernumber,bookingId,hotelimage!!)
                            myreservation.push().setValue(reservationDetail)
                            refernce.child(username).push().setValue(reservationDetail)
                            val id=transactionid.push().key
                            val transactionDetail=TransactionDetail(stringtxnid)
                            transactionid.child(id!!).setValue(transactionDetail)
                            val intent= Intent(applicationContext,FinalPaymentConfirm::class.java)
                            intent.putExtra("ordernumber",ordernumber)
                            intent.putExtra("finalamount",totalprice)
                            intent.putExtra("hotelname",hotelname)
                            intent.putExtra("roomtypeof",roomtype)
                            intent.putExtra("howmayrooms",roomsize)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createrandomordernumber(i:Int):String{
        var j=i
       var character:String="1234567890"
        var builder=StringBuilder()
        while (j>0){
            val random=Random()
            builder.append(character[random.nextInt(character.length)])
            j--
        }
        return builder.toString()
    }
    private fun BookingIdGenerator(i:Int):String{
        var j=i
        val character="1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val builder=StringBuilder()
        while (j>0){
            val random=Random()
            builder.append(character[random.nextInt(character.length)])
            j--
        }
        return builder.toString()
    }
}