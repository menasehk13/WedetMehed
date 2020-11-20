package com.example.wedetmehed

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class ReservationClassAdapter(var context: Context,var list:List<Resarvation>):
    RecyclerView.Adapter<ReservationClassAdapter.viewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.custom_my_booking,parent,false)
        return viewholder(view)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.hotelnameof.text=list[position].hotelname
        holder.chekindate.text=list[position].checkindate
        holder.checkoutdate.text=list[position].checkoutdate
        Picasso.get().load(list[position].hotelimage).fit().into(holder.imageView)
        holder.cardview.setOnClickListener(){
            val bookingid=list[position].bookingId
            val intent= Intent(it.context,DisplayMyReservation::class.java)
            intent.putExtra("bookingid",bookingid)
            it.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class viewholder( itemView: View): RecyclerView.ViewHolder(itemView) {
     val imageView: ImageView =itemView.findViewById<ImageView>(R.id.myreservedhotel)
        val hotelnameof: TextView =itemView.findViewById<TextView>(R.id.hotelnamereserved)
        val chekindate: TextView =itemView.findViewById<TextView>(R.id.mycheckindate)
        val checkoutdate: TextView =itemView.findViewById<TextView>(R.id.mycheckoutdate)
        val cardview:CardView=itemView.findViewById(R.id.mycardview)
    }
}