package com.example.wedetmehed

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class SelectRoomActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var roomrecycleview:RecyclerView
    private lateinit var imageofhotel: ImageView
    private lateinit var roomClassAdapter: RoomClassAdapter
     var detail:ArrayList<RoomClassDetail> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_room)
        textView=findViewById(R.id.userselecthotlname)
        textView.text=intent.getStringExtra("hotel")
        roomrecycleview=findViewById(R.id.recycleroomselect)
        val layoutManager=LinearLayoutManager(applicationContext,LinearLayoutManager.VERTICAL,false)
        roomrecycleview.layoutManager=layoutManager
        imageofhotel=findViewById(R.id.userselecthotelimage)
        val hotelname=intent.getStringExtra("hotel")
        roomClassAdapter=RoomClassAdapter(detail,applicationContext,hotelname!!)
        val databaseReference=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelDetail").child(hotelname!!)
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val hotelClassDetail=snapshot.getValue((HotelClassDetail::class.java))
                Picasso.get().load(hotelClassDetail?.imageurl).fit().into(imageofhotel)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        val roomreference=databaseReference.child("RoomTpe")
        roomreference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for (datasnapshot:DataSnapshot in snapshot.children){
                   val roomClassDetail=datasnapshot.getValue(RoomClassDetail::class.java)
                   detail.add(roomClassDetail!!)
               }
                roomrecycleview.adapter=roomClassAdapter
                roomClassAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}