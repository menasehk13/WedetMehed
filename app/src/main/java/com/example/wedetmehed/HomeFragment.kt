package com.example.wedetmehed

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var username:TextView
    private lateinit var mapButton: ImageButton
    lateinit var recentrecycleview:RecyclerView
    lateinit var hotelClassAdapter: HotelClassAdapter
    var detail:ArrayList<HotelClassDetail> = ArrayList()
     private lateinit var mdatabase:FirebaseDatabase
    private lateinit var databaseReference1: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        username=view.findViewById(R.id.username)
        mapButton=view.findViewById(R.id.mapbutton)
       val hotelrecycleView=view.findViewById(R.id.recyleviewhotel) as RecyclerView
        val prefamanger=PrefManager(this.requireContext())
        username.text= "Hi"+" "+prefamanger.getusername()
        val user=FirebaseAuth.getInstance()
        val uId:String=user.uid.toString()
        val collection=FirebaseFirestore.getInstance().collection("Users").document(uId)
        collection.get().addOnCompleteListener(){
            val documentReference=it.result
            if (documentReference.exists()){
                Log.d("File exsits", "onViewCreated: ")
            }
        }

        val animation1:Animation=AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        username.animation=animation1
        mapButton.setOnClickListener(){
            val intent= Intent(context, MapActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            )
        }
        hotelrecycleView.layoutManager=LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        hotelrecycleView.hasFixedSize()
        hotelClassAdapter=HotelClassAdapter(detail, context, requireActivity())

        val databaseReference=FirebaseDatabase.getInstance().reference.child("Hotels").child("HotelDetail")
          databaseReference.keepSynced(true)
          val listiner=object :ValueEventListener{
              override fun onDataChange(snapshot: DataSnapshot) {

                        for (datasnapshot in snapshot.children){
                            val hoteldetail=datasnapshot.getValue(HotelClassDetail::class.java)
                            Log.d("Values", "onDataChange:" + hoteldetail.toString())
                            if (hoteldetail != null) {
                                detail.add(hoteldetail)
                            }
                        }

                  hotelrecycleView.adapter=hotelClassAdapter

              }

              override fun onCancelled(error: DatabaseError) {

              }

          }
        databaseReference.addValueEventListener(listiner)
    }

}