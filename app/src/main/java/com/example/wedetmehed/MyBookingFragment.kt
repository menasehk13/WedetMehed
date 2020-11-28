package com.example.wedetmehed

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.common.collect.Maps
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class MyBookingFragment : Fragment() {
    private lateinit var recyclerView:RecyclerView
    private lateinit var reservationClassAdapter:ReservationClassAdapter
    var list:ArrayList<Resarvation> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_booking,container,false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       recyclerView=view.findViewById(R.id.recyleviewbooking)
       recyclerView.layoutManager=LinearLayoutManager(context)
        reservationClassAdapter= ReservationClassAdapter(requireContext(),list)

        val linearLayout=view.findViewById<LinearLayout>(R.id.ifemptyrecyle)

        val findnearby=view.findViewById<Button>(R.id.findnearbyfrombooking)
        findnearby.setOnClickListener(){
            val intent= Intent(context,MapActivity::class.java)
            startActivity(intent)
        }
        val stringuid=FirebaseAuth.getInstance().currentUser?.uid
        val database=FirebaseDatabase.getInstance().reference.child("Hotels").child("Myreservation").child(stringuid!!)
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    linearLayout.visibility=View.INVISIBLE
                }else{
                    linearLayout.visibility=View.VISIBLE
                }
                list.clear()
                for (datasnapshot:DataSnapshot in snapshot.children) {

                        val resarvation = datasnapshot.getValue(Resarvation::class.java)
                        list.add(resarvation!!)
                }
                recyclerView.adapter=reservationClassAdapter
                reservationClassAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}