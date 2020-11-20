package com.example.wedetmehed

import kotlin.properties.Delegates

class HotelClassDetail() {
     var rating by Delegates.notNull<Float>()
     var imageurl:String? = null
    var hotelname:String? = null
    var price by Delegates.notNull<Int>()
}