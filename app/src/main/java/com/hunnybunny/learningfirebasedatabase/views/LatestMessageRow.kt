package com.hunnybunny.learningfirebasedatabase.views

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hunnybunny.learningfirebasedatabase.R
import com.hunnybunny.learningfirebasedatabase.models.Chatmessage
import com.hunnybunny.learningfirebasedatabase.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageRow(val chatmessage: Chatmessage): Item<ViewHolder>()
{
    var chatPartnerUser: User? =null
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.messagetextview_latest_message.text=chatmessage.text
        ///fromiD == currentuser
        ///toId ===opponent user
//        var samnevalekiid:String
        val chatPartnerId: String
        if(chatmessage.fromId== FirebaseAuth.getInstance().uid)
        {
            chatPartnerId=chatmessage.toId
        }
        else
        {

            chatPartnerId=chatmessage.fromId
        }
        Log.d("FromID",chatmessage.fromId)
        Log.d("ToID",chatmessage.toId)
        Log.d("partnerid",chatPartnerId)
        var displayname:String
        var samnevalekiprofile:String
        val ref= FirebaseDatabase.getInstance().reference.child("Users").child(chatPartnerId)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
//                chatPartnerUser=data.getValue(User::class.java)
                displayname=data.child("display_name").value.toString()
                samnevalekiprofile=data.child("proifleimageUrl").value.toString()
                viewHolder.itemView.usernametextView_latest_message.text=displayname
                Picasso.get().load(samnevalekiprofile)
                        .placeholder(R.drawable.ic_baseline_account_circle_24)
                        .into(viewHolder.itemView.imageView_latest_message_dashboard)
                chatPartnerUser= User(data.child("uid").value.toString(),displayname,samnevalekiprofile)

                Log.d("LatestMesssage", "Current user ${chatPartnerUser!!.uid}1   ${chatPartnerUser!!.profileimageurl} 2   ${chatPartnerUser!!.username}  ")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun getLayout(): Int {
//            TODO("Not yet implemented")
        return R.layout.latest_message_row
    }
}