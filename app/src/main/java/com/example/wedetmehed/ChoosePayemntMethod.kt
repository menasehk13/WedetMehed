package com.example.wedetmehed

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ChoosePayemntMethod : AppCompatActivity() {
    private lateinit var payinhotel: CheckBox
    private lateinit var payusingmobile: CheckBox
    private lateinit var payusingpaypal: CheckBox
    private lateinit var proccedtopayment: Button
    private var hotelname: String? = null
    private var roomtype: String? = null
    private var roomsize: String? = null
    private var nights: String? = null
    private var checkindate: String? = null
    private var checkoutdate: String? = null
    private var totalprice: String? = null
    private var hotelimage: String? = null
    private var TAG = "Value"
    private lateinit var alertDialog: AlertDialog

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_payemnt_method)
        val radioGroup = findViewById<RadioGroup>(R.id.radiogrouppay)
        val int=radioGroup.checkedRadioButtonId
        val radioButton=findViewById<RadioButton>(R.id.payusingmobile)
        hotelname=intent.getStringExtra("hotelofuser")
        roomtype=intent.getStringExtra("roomtypeof")
        roomsize=intent.getStringExtra("roomsizeselected")
        nights=intent.getStringExtra("hownamynights")
        checkindate=intent.getStringExtra("checkindate")
        checkoutdate=intent.getStringExtra("checkoutdate")
        totalprice=intent.getStringExtra("priceTotal")
        hotelimage=intent.getStringExtra("imageof")
       proccedtopayment=findViewById(R.id.confirmpayment)
        proccedtopayment.setOnClickListener {
            if (radioGroup.checkedRadioButtonId==-1){
                Toast.makeText(this,"Choose Payment",Toast.LENGTH_LONG).show()
            }
            if (radioButton.isChecked){
                val inten2=Intent(this,PayUsingMobile::class.java)
                inten2.putExtra("hotelofuser", hotelname)
                inten2.putExtra("roomtypeof", roomtype)
                inten2.putExtra("roomsizeselected", roomsize)
                inten2.putExtra("checkindate", checkindate)
                inten2.putExtra("checkoutdate", checkoutdate)
                inten2.putExtra("hownamynights", nights)
                inten2.putExtra("priceTotal", totalprice)
                inten2.putExtra("imageof",hotelimage)
                inten2.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP;
                startActivity(inten2)
                finish()
            }

            radioGroup.setOnCheckedChangeListener { group, checkedId ->
                when(checkedId){
                    R.id.payusingmobile->{


                    }
                }
            }
        }

    }


}