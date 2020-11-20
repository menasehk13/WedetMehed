package com.example.wedetmehed

import kotlin.properties.Delegates

class RoomClassDetail{
      var roomimage:String?=null
    var roomprice by Delegates.notNull<Int>()
    lateinit var roomsize:String
    lateinit var roomtype:String
}