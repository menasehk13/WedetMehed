package com.example.wedetmehed

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class FinalPaymentConfirm : AppCompatActivity() {
    private lateinit var ordernumber:TextView
    private lateinit var FinalAmount:TextView
    private lateinit var hotelnameof:TextView
    private lateinit var roomtypeandsize:TextView
    private lateinit var backtohomebutton: Button
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final_payment_confirm)
        ordernumber=findViewById(R.id.ordernumber)
        FinalAmount=findViewById(R.id.amountpayed)
        hotelnameof=findViewById(R.id.orderdhotel)
        roomtypeandsize=findViewById(R.id.orderedroomtype)
         ordernumber.text=intent.getStringExtra("ordernumber")
        FinalAmount.text=intent.getStringExtra("finalamount")
        hotelnameof.text=intent.getStringExtra("hotelname")
        roomtypeandsize.text=intent.getStringExtra("roomtypeof")+" * "+intent.getStringExtra("howmayrooms")
        backtohomebutton=findViewById(R.id.backtohome)
        backtohomebutton.setOnClickListener(){
            val intent=Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}