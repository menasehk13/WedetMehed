package com.example.wedetmehed

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RoomClassAdapter(var detail:List<RoomClassDetail>,var context: Context,val hotelname:String):RecyclerView.Adapter<RoomClassAdapter.viewholder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.custom_room_types,parent,false)
        return viewholder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: viewholder, position: Int) {
      holder.roomtypename.text= detail[position].roomtype
        holder.roomtypeprice.text=detail[position].roomprice.toString()+"Br"
        Picasso.get().load(detail[position].roomimage).fit().into(holder.roomimage)
         holder.cardView.setOnClickListener(){
             val intent= Intent(it.context,SelectDateActivity::class.java)
             val roomtype=holder.roomtypename.text.toString()
             intent.putExtra("roomtype",roomtype)
             intent.putExtra("hotelnameof",hotelname)
             it.context.startActivity(intent)
         }
    }

    override fun getItemCount(): Int {
        return detail.size
    }
    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView=itemView.findViewById<CardView>(R.id.roomcardview)
      val roomtypename=itemView.findViewById<TextView>(R.id.roomtype)
        val roomtypeprice=itemView.findViewById<TextView>(R.id.priceofroom)
        val roomimage=itemView.findViewById<ImageView>(R.id.roomimages)

    }
}