package com.hunnybunny.learningfirebasedatabase.messages

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.hunnybunny.learningfirebasedatabase.NewMessageActivity
import com.hunnybunny.learningfirebasedatabase.NewMessageActivity.Companion.USER_KEY
import com.hunnybunny.learningfirebasedatabase.R
import com.hunnybunny.learningfirebasedatabase.messages.DashboardActivity.Companion.currentUser
import com.hunnybunny.learningfirebasedatabase.models.Chatmessage
import com.hunnybunny.learningfirebasedatabase.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class ChatLogActivity : AppCompatActivity() {
    lateinit var mFirebaseDatabaseReference: DatabaseReference
    lateinit var mAuth:FirebaseAuth

    var touser:User?=null
    var profileimagepicuserki:String=""
    var adapter=GroupAdapter<ViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        mFirebaseDatabaseReference=FirebaseDatabase.getInstance().reference
        mAuth=Firebase.auth
        messageRecyclerView.adapter=adapter
        touser=intent.getParcelableExtra<User>(USER_KEY)
        supportActionBar?.title=touser?.username


        Log.d("meraname", touser!!.username)
        Log.d("meraid", touser!!.uid)
        Log.d("meraimage", touser!!.profileimageurl)

        listenformessage()
        send_btn.setOnClickListener {
            Performsendmessage()
        }
    }


    private fun listenformessage()
    {
        var fromId_path=FirebaseAuth.getInstance().uid
        var toId_path=touser?.uid
        val ref=FirebaseDatabase.getInstance().reference.child("User-Messages").child("$fromId_path").child(
                "$toId_path"
        )
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(data: DataSnapshot, previousChildName: String?) {
                var chatMessage = data.getValue(Chatmessage::class.java)
//                val text = data.child("text").value.toString()
//                var fromId = data.child("fromId").value.toString()
                var time = chatMessage!!.timeStamp

//                Log.d("time",time.hours.toString())

                if (chatMessage != null) {
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(chatToItem(chatMessage.text, currentUser!!))
                    } else {
                        adapter.add(chatFromItem(chatMessage.text, touser!!))
                    }
                    Log.d("listenbro", "${touser?.uid} 1    ${currentUser?.uid}")
                }

                messageRecyclerView.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun Performsendmessage()
    {

        var fromId=FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user?.uid
        if(fromId==null)return
        var reference=FirebaseDatabase.getInstance().reference.child("User-Messages").child("$fromId").child(
                "$toId"
        ).push()
        Log.d("lastbar", "$fromId 1    $toId")
        val toref=FirebaseDatabase.getInstance().reference.child("User-Messages").child("$toId").child(
                "$fromId"
        ).push()


        var text:String=edittext_chat.text.toString()
        edittext_chat.text.clear()

        val calendar = Calendar.getInstance()
        val hour24hrs = calendar[Calendar.HOUR_OF_DAY]
        val hour12hrs = calendar[Calendar.HOUR]
        val minutes = calendar[Calendar.MINUTE]
        var time="$hour12hrs:$minutes"
        Log.d("time",time.toString())
        val chatmessage= Chatmessage(
                reference.key!!,
                text,
                fromId,
                toId!!,
                time
        )
        reference.setValue(chatmessage)
                .addOnSuccessListener {
                    messageRecyclerView.scrollToPosition(adapter.itemCount - 1)
                }
        toref.setValue(chatmessage)
        val latestmessgaesref=FirebaseDatabase.getInstance().reference.child("latest-messages").child(
                "$fromId"
        ).child("$toId")
        latestmessgaesref.setValue(chatmessage)

        val latestmessgaesToref=FirebaseDatabase.getInstance().reference.child("latest-messages").child(
                "$toId"
        ).child("$fromId")
        latestmessgaesToref.setValue(chatmessage)
    }

}
class chatFromItem(val text: String, val user: User):Item<ViewHolder>()
{
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text=text
        if (user.profileimageurl.isEmpty())
        {
            Log.d("Fromitem", user.profileimageurl)
            return
        }
        Picasso.get().load(user.profileimageurl)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(viewHolder.itemView.imageViewfromrow)
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}
class chatToItem(val text: String, val user: User):Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
        if (user.profileimageurl.isEmpty()) {
            Log.d("Toitem", user.profileimageurl)
            return
        }
        Picasso.get().load(user.profileimageurl)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(viewHolder.itemView.imageViewtorow)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}
