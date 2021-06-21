package com.hunnybunny.learningfirebasedatabase.models


class Chatmessage(val id :String,val text:String,val fromId:String,val toId:String,val timeStamp:String)
{
    constructor():this("","","","","")
}