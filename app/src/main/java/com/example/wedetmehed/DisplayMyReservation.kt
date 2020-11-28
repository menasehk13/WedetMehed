package com.example.wedetmehed

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGContents.Type.TEXT
import androidmads.library.qrgenearator.QRGEncoder
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.oned.Code128Writer
import com.google.zxing.qrcode.QRCodeWriter
import com.squareup.picasso.Picasso

class DisplayMyReservation : AppCompatActivity() {
    lateinit var hotelname:TextView
    lateinit var rootype:TextView
    lateinit var hotelimage:ImageView
    lateinit var barcodeimage:ImageView
    lateinit var displaycheckin:TextView
    lateinit var displaycheckout:TextView
    lateinit var displaybookingid:TextView
    lateinit var displaytxnid:TextView
    lateinit var cancelbooking: Button
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_my_reservation)
        supportActionBar?.hide()
        hotelname=findViewById(R.id.displayhotelname)
        rootype=findViewById(R.id.displayroomtype)
        hotelimage=findViewById(R.id.displayimage)
        barcodeimage=findViewById(R.id.displaybarcode)
        displaycheckin=findViewById(R.id.displaycheckin)
        displaycheckout=findViewById(R.id.displaycheckout)
        displaybookingid=findViewById(R.id.displaybookingid)
        displaytxnid=findViewById(R.id.displaytxnid)
        cancelbooking=findViewById(R.id.cancelbooking)
        val bookingid=intent.getStringExtra("bookingid")
        displaybookingid.text= "Booking ID : $bookingid"
        val stringid=FirebaseAuth.getInstance().currentUser?.uid
        val query=FirebaseDatabase.getInstance().reference.child("Hotels").child("Myreservation").child(stringid!!).orderByChild("bookingId").equalTo(bookingid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onDataChange(snapshot: DataSnapshot) {
                for (datesnapshot: DataSnapshot in snapshot.children) {
                    val reservation = datesnapshot.getValue(Resarvation::class.java)
                     Picasso.get().load(reservation!!.hotelimage).fit().into(hotelimage)
                    hotelname.text=reservation.hotelname
                    displaycheckin.text=reservation.checkindate
                    displaycheckout.text=reservation.checkoutdate
                    rootype.text=reservation.roomtype
                    val txnid=reservation.txnid
                    displaybitmap(txnid)
                    displaytxnid.text=txnid
                    cancelbooking.setOnClickListener(){
                      datesnapshot.ref.removeValue()
                        onBackPressed()
                    }

                }
            }


            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun displaybitmap(txnid: String) {
        val qrgEncoder= QRGEncoder(txnid,null, TEXT,500)
        try {
            val bitmap:Bitmap=qrgEncoder.bitmap
            barcodeimage.setImageBitmap(bitmap)
        }catch (e:WriterException){
            Log.d("value", "displaybitmap: "+e.message)
        }



        /*val widthPixels = resources.getDimensionPixelSize(R.dimen.width_barcode)
        val heightPixels = resources.getDimensionPixelSize(R.dimen.height_barcode)
        barcodeimage.setImageBitmap(
            createrBarcodeBitmap(
                barcodeValue = txnid,
                barcodeColor = getColor(android.R.color.black),
                backgroundColor = getColor(android.R.color.white),
                widthPixels = widthPixels,
                heightPixels = heightPixels
            )
        )

         */
    }

    private fun createrBarcodeBitmap(
        barcodeValue: String,
        barcodeColor: Int,
        backgroundColor: Int,
        widthPixels: Int,
        heightPixels: Int,
    ): Bitmap {
        val bitMatrix = QRCodeWriter().encode(
            barcodeValue,
            BarcodeFormat.QR_CODE,
            widthPixels,
            heightPixels
        )
        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height) {
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )
        return bitmap
    }
}