package com.hunnybunny.learningfirebasedatabase.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.hunnybunny.learningfirebasedatabase.registerlogin.MainActivity
import com.hunnybunny.learningfirebasedatabase.NewMessageActivity
import com.hunnybunny.learningfirebasedatabase.NewMessageActivity.Companion.USER_KEY
import com.hunnybunny.learningfirebasedatabase.R
import com.hunnybunny.learningfirebasedatabase.UserItem
import com.hunnybunny.learningfirebasedatabase.models.Chatmessage
import com.hunnybunny.learningfirebasedatabase.models.User
import com.hunnybunny.learningfirebasedatabase.views.LatestMessageRow
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.dashboard_activiyt.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

//////////////////////////////////////////LATEST MESSAGE ACTIVITY/////////////////////////////////////
class DashboardActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    var profileimageuserki:String=""
    var usernameuserka:String=""
    var profileuserkifromdashboardtochatlog:String=""

//    var samnevalekaname:String=""
//    var samnewalakaprofilepic:String=""
//    var samnewalekaid:String=""

    var adapter=GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activiyt)
        mDatabaseReference= FirebaseDatabase.getInstance().reference
        auth=Firebase.auth
//        val currentuser = auth.currentUser
//        var userid=currentuser!!.uid
//        Log.d("Sanjaja",userid.toString())
        supportActionBar?.title="Dashboard"

//        setSupportActionBar(toolbar2)
        recyclerview_latest_message.adapter=adapter
        recyclerview_latest_message.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        listenforlatestmessage()
        fetchCurrentUser()

        if(usernameuserka.isEmpty())
        {
            supportActionBar!!.title ="DashBoard"
        }
        else
        {
            supportActionBar!!.title=usernameuserka
        }


        adapter.setOnItemClickListener { item, view ->
            val intent=Intent(this,ChatLogActivity::class.java)
            val row=item as LatestMessageRow
//            Log.d("BHAIplsdede","${row.chatPartnerId!!.profileimageurl}1   ${row.chatPartnerId!!.username}")

            intent.putExtra(USER_KEY,row.chatPartnerUser)

            Log.d("imagehmeri", profileimageuserki)
//            intent.putExtra("samnekiIdfromDASHBOARD",samnewalekaid)
//            intent.putExtra("samneanamefromDASHBOARD",samnevalekaname)
//            intent.putExtra("sanmneprofileimagefromDASHBOARD", samnewalakaprofilepic)
            startActivity(intent)
//            fetchCurrentUser()
        }

    }

    val latestMessagesMap=HashMap<String,Chatmessage>()


    /**sssss*/
    private fun refereshrecyclermessage()
    {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }


    private fun listenforlatestmessage()
    {
        var fromId=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().reference.child("latest-messages").child("$fromId")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(data: DataSnapshot, previousChildName: String?) {
                val chatMessage = data.getValue(Chatmessage::class.java)
//                var key: String? =data.key
//                latestMessagesMap[key] = chatMessage
                latestMessagesMap[data.key!!]=chatMessage!!
                Log.d("keyy","${data.key}")
                refereshrecyclermessage()
//                adapter.add(LatestMessageRow(chatMessage))
            }

            override fun onChildChanged(data: DataSnapshot, previousChildName: String?) {
                val chatMessage=data.getValue(Chatmessage::class.java)
                latestMessagesMap[data.key!!]=chatMessage!!
                refereshrecyclermessage()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.newMessage ->
            {

            startActivity(Intent(this, NewMessageActivity::class.java))

            }
            R.id.Signout ->
            {
                auth.signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.Profile->
            {
                    val intent=Intent(this,aboutdeveloper::class.java)
                intent.putExtra("UserData", currentUser)
                    startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu,menu)
        return super.onCreateOptionsMenu(menu)
//        return t
    }

    private fun fetchCurrentUser() {
//        Toast.makeText(this,"aagaya andr",Toast.LENGTH_LONG).show()
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")
        ref.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(data: DataSnapshot) {

                currentUser = User(data.child("uid").value.toString(),data.child("display_name").value.toString(),data.child("proifleimageUrl").value.toString())
//                profileimageuserki=data.child("proifleimageUrl").value.toString()
//                usernameuserka=data.child("display_name").value.toString()
                Log.d("LatestMessages", "Current user $profileimageuserki 1   $usernameuserka 2 ")
                if(currentUser==null)
                {
                    supportActionBar!!.title ="DashBoard"
                }
                else
                {
                    supportActionBar!!.title= currentUser!!.username
                }
//                var userurl= currentUser!!.profileimageurl

//                currentUser.profileimageurl
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
//        Log.d("meraname", currentUser!!.username)
//        Log.d("meraid", currentUser!!.uid)
//        Log.d("meraimage", currentUser!!.username)
    }


}