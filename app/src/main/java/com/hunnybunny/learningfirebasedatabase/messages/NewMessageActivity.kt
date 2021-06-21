package com.hunnybunny.learningfirebasedatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.hunnybunny.learningfirebasedatabase.messages.ChatLogActivity
import com.hunnybunny.learningfirebasedatabase.models.User
import com.hunnybunny.learningfirebasedatabase.registerlogin.RegisterActivity
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var mDatabaseRef:DatabaseReference
    var profileimagepicDashboard=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar!!.title="Select Users"
        auth=Firebase.auth
        mDatabaseRef= FirebaseDatabase.getInstance().reference

//        val adapter= GroupAdapter<ViewHolder>()////check ittttt import me kuch or puch rha tha
//
//
//        recyclerview_newmessage.adapter=adapter
        fetchUsers()
//        fetchCurrentUser()
    }
//    companion object {
//        val USER_KEY = "USER_KEY"
//    }


//    private fun fetchCurrentUser() {
////        val uid = FirebaseAuth.getInstance()
//        var uid=FirebaseAuth.getInstance().uid
//        if(uid!=null){
//            val ref = FirebaseDatabase.getInstance().getReference("/Users/$uid")
//            ref.addListenerForSingleValueEvent(object: ValueEventListener {
//
//                override fun onDataChange(data: DataSnapshot) {
//                    profileimagepicDashboard=data.child("proifleimageUrl").value.toString()
//
////                currentUser = p0.getValue(User::class.java)
////                Log.d("LatestMessages", "Current user ${currentUser?.profileImageUrl}")
//                }
//
//                override fun onCancelled(p0: DatabaseError) {
//
//                }
//            })
//        }
//    }


    companion object{
        val USER_KEY="USER_KEY"
    }
    private fun fetchUsers()
    {
        mDatabaseRef.child("Users").addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(data: DataSnapshot) {
                val adapter=GroupAdapter<ViewHolder>()

         data.children.forEach {
         val user= it.getValue(User::class.java)
             var datauser= User(it.child("uid").value.toString(),it.child("display_name").value.toString(),it.child("proifleimageUrl").value.toString())
             if(user!= null)
             {
                 adapter.add(UserItem(datauser))
             }
               }
                adapter.setOnItemClickListener { item, view ->
                    val useritem=item as UserItem
//                    if(profileimagepicDashboard.isEmpty()) {
//                        Toast.makeText(this@NewMessageActivity,"RukoJaraSabarKro",Toast.LENGTH_LONG).show()
//                        return@setOnItemClickListener
//                    }
                val intent= Intent(view.context,ChatLogActivity::class.java)
                              intent.putExtra(USER_KEY,useritem.user)
                    startActivity(intent)
                    finish()
                }
                recyclerview_newmessage.adapter=adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}
class UserItem(var user:User):Item<ViewHolder>()
{
    override fun bind(viewHolder: ViewHolder, position: Int) {
    viewHolder.itemView.textView_newmessage.text=user.username
        Picasso.get().load(user.profileimageurl)
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(viewHolder.itemView.imageview)
    }
    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}
