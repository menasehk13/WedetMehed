package com.example.wedetmehed

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class HotelClassAdapter(val detail: List<HotelClassDetail>, val context: Context?,val activity:FragmentActivity): RecyclerView.Adapter<HotelClassAdapter.viewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val layoutInflater=LayoutInflater.from(parent.context).inflate(R.layout.custom_view_hotels,parent,false)
        return viewholder(layoutInflater)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.hotelname1.text= detail[position].hotelname
        holder.ratingBar.rating=detail[position].rating
        Picasso.get().load(detail[position].imageurl).fit().into(holder.imageurl)
        holder.cardView.setOnClickListener(){
            val hotelvalue=holder.hotelname1.text.toString()
            val intent=Intent(it.context,ViewDetailActivity::class.java)
            intent.putExtra("hotelvalue",hotelvalue)
            activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            it.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return detail.size
    }
    class viewholder(view: View) : RecyclerView.ViewHolder(view){
         val hotelname1:TextView=view.findViewById(R.id.hotelname)
        val ratingBar:RatingBar=view.findViewById(R.id.hotelrating)
        val imageurl:ImageView=view.findViewById(R.id.hotelimage)
        val cardView:CardView=view.findViewById(R.id.cardhotel)
    }
}