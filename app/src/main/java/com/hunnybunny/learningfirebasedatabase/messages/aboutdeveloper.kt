package com.hunnybunny.learningfirebasedatabase.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.hunnybunny.learningfirebasedatabase.NewMessageActivity
import com.hunnybunny.learningfirebasedatabase.R
import com.hunnybunny.learningfirebasedatabase.models.User
import com.hunnybunny.learningfirebasedatabase.registerlogin.MainActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_aboutdeveloper.*
import kotlinx.android.synthetic.main.user_row_new_message.*

class aboutdeveloper : AppCompatActivity() {
    var myprofileuser: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutdeveloper)

        myprofileuser = intent.getParcelableExtra("UserData")
        supportActionBar!!.title = "User Profile"
        supportActionBar!!.hide()
        name.text = myprofileuser!!.username
        Picasso.get().load(myprofileuser!!.profileimageurl)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .into(imageView)
//
//      var  toolbar = (Toolbar(this))findViewById(R.id.toolbar);
//        toolbar.setSubtitle("Test Subtitle");
//        toolbar.inflateMenu(R.menu.main_manu);
//
//    }

//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId)
//        {
//            R.id.newMessage ->
//            {
//
//                startActivity(Intent(this, NewMessageActivity::class.java))
//
//            }
//            R.id.Signout ->
//            {
////                auth.signOut()
////                startActivity(Intent(this, MainActivity::class.java))
////                finish()
//            }
//            R.id.Profile->
//            {
//                val intent= Intent(this,aboutdeveloper::class.java)
//                intent.putExtra("UserData", DashboardActivity.currentUser)
//                startActivity(intent)
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.dashboard_menu,menu)
//        return super.onCreateOptionsMenu(menu)
////        return t
//    }

    }
}