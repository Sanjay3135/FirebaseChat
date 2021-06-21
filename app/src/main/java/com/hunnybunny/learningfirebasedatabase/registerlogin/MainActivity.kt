package com.hunnybunny.learningfirebasedatabase.registerlogin

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hunnybunny.learningfirebasedatabase.R
import com.hunnybunny.learningfirebasedatabase.messages.DashboardActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
//    lateinit var mStorageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        mStorageeref=FirebaseStorage.getInstance().reference
//        supportActionBar!!.title="FirebaseChat"
        auth=Firebase.auth


//        auth = Firebase.auth
//        authListener= AuthStateListener {
//            firebaseAuth: FirebaseAuth ->
////            auth.currentUser
//            user= firebaseAuth.currentUser!!
//            if(user!=null){
//                startActivity(Intent(this,AllMembers::class.java))
//            }
//            else
//            {
//                Toast.makeText(this,"Sign In please",Toast.LENGTH_SHORT).show()
//            }
//
//        }

        register.setOnClickListener {
            val intent=Intent(this, RegisterActivity::class.java)
            startActivity(intent)
//            finish()
        }



        btn_login.setOnClickListener {
            if(edt1.text!!.isEmpty())
            {
                edt1.setError("Enter Email")
                return@setOnClickListener
            }

            if(edt2.text!!.isEmpty())
            {
                 edt2.setError("Enter Password")
                return@setOnClickListener
            }

            val pd= ProgressDialog(this)
            pd.setTitle("Hold a Minute")
            pd.show()
            val email=edt1.text.toString()
            val password=edt2.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Sanjay", "signInWithEmail:success")
                        val user = auth.currentUser
                        pd.dismiss()
                        var username=email.split("@")[0]
                        Toast.makeText(this,"Logged In Succesfull",Toast.LENGTH_SHORT).show()
                        var dashboard=Intent(this, DashboardActivity::class.java)
                        dashboard.putExtra("name",username)
                        startActivity(dashboard)
                        finish()
//                        updateUI(user)
                    } else {
                        pd.dismiss()
                        // If sign in fails, display a message to the user.
//                        Log.w("Sanjay", "signInWithEmail:failure", task.exception)
                        Toast.makeText(this,"Try Again Or Create New Account",Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    override fun onStart() {
        if(auth.currentUser!=null)
        {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
        super.onStart()
    }
}