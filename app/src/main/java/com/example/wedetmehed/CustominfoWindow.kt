package com.example.wedetmehed

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import org.w3c.dom.Text

class CustominfoWindow (val context:Context):GoogleMap.InfoWindowAdapter{
    private lateinit var inflater:LayoutInflater
    private lateinit var hotelClassDetail: HotelClassDetail

    fun rendowindowtext(marker: Marker,view: View){
        hotelClassDetail=marker.tag as HotelClassDetail
        var price=view.findViewById<TextView>(R.id.price)
        price.text=hotelClassDetail.price.toString()
        val hotelname=hotelClassDetail.hotelname
        marker.title=hotelname
    }
    override fun getInfoWindow(p0: Marker): View {
        inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view=inflater.inflate(R.layout.custom_info_window,null)
        rendowindowtext(p0,view)
        return view
    }

    @SuppressLint("InflateParams")
    override fun getInfoContents(p0: Marker): View {
        inflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view=inflater.inflate(R.layout.custom_info_window,null)
        rendowindowtext(p0,view)
        return view
    }

}