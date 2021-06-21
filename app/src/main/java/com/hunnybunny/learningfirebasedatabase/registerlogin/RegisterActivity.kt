package com.hunnybunny.learningfirebasedatabase.registerlogin

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hunnybunny.learningfirebasedatabase.R
import com.hunnybunny.learningfirebasedatabase.messages.DashboardActivity
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.view.*
import java.io.ByteArrayOutputStream
import java.util.*


class RegisterActivity : AppCompatActivity() {

    lateinit var mStorageeref: StorageReference
    lateinit var mDatabase: DatabaseReference
    lateinit var auth : FirebaseAuth
    var profilepic:String="defaultpiclink"
    var tempint:Int=0
private var filepath: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mStorageeref= FirebaseStorage.getInstance().reference
        auth =Firebase.auth
        mDatabase=FirebaseDatabase.getInstance().reference
        supportActionBar!!.title="FirebaseChat"
      imgchooser.setOnClickListener {
            tempint=1
            var intent= Intent()
            intent.type="image/*"
            intent.action= Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 100)
        }

//        if(edt_name.text!!.isEmpty())
//        {
//            edt_name.setError("Required")
//        }


//        var binding=ScriptGroup.Binding



        reg_btn.setOnClickListener{

            var pd=ProgressDialog(this)
            pd.setTitle("Uploading Data")
            pd.show()
            if(edt_name.text!!.isEmpty())
            {
                pd.dismiss()
                edt_name.setError("Enter Username")
                return@setOnClickListener
            }

        if(edt_email.text!!.isEmpty())
        {
            pd.dismiss()
            edt_email.setError("Enter Email")
            return@setOnClickListener
        }

        if(edt_password.text!!.isEmpty())
        {
            pd.dismiss()
            edt_password.setError("Enter Password")
            return@setOnClickListener
        }
            if(filepath==null)
            {
                pd.dismiss()
                Toast.makeText(this, "Upload Image", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

        val email=edt_email.text.toString()
        val password=edt_password.text.toString()
        val displayname=edt_name.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Sanjay", "createUserWithEmail:success")
                    val currentuser = Firebase.auth.currentUser
                    var userid=currentuser!!.uid


                    mDatabase= FirebaseDatabase.getInstance().reference
                        .child("Users").child(userid)
                    var userobject= HashMap<String, String>()
                    userobject.put("proifleimageUrl", profilepic)
                    userobject.put("uid", userid)
                    userobject.put("display_name", displayname)


                    mDatabase.setValue(userobject).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            pd.dismiss()

                            Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
//                                Toast.makeText(this,"Account Created",Toast.LENGTH_SHORT).show()
                            val intent=Intent(this, DashboardActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        else
                        {
                            pd.dismiss()
                            Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show()
                        }
                    }


//                        updateUI(user)
                } else {
                    pd.dismiss()
                    // If sign in fails, display a message to the user.
                    Log.w("Sanjay", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Enter Valid Email or Password",
                            Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                }
            }
    }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if(requestCode==100&&resultCode== Activity.RESULT_OK)
        {
            filepath= data!!.data!!
            var bitmap= MediaStore.Images.Media.getBitmap(contentResolver, filepath)
//            imageView.setImageURI(filepath)
            imageView.setImageBitmap(bitmap)
            CropImage.activity(filepath)
                    .setAspectRatio(1, 1)
                    .start(this)


//            mStorageeref.child("My images").child("$userId.jpg").downloadUrl.addOnSuccessListener {
//                var downloadurl=it.toString()
//            }
//                    .addOnFailureListener {
//                        Toast.makeText(this,"Try Again",Toast.LENGTH_LONG).show()
//                    }
//
//            var pd=ProgressDialog(this)
//            pd.setTitle("Saving Image To Database")
//            pd.show()
////           pd.progress=imageView.
////            val userId = UUID.randomUUID().toString()
//            var userId=UUID.randomUUID().toString()
////            Log.d("Sanjaaa", userId)
//            var imageref=   mStorageeref.child("My images").child("$userId.jpg")
//            imageref.putFile(filepath!!).addOnSuccessListener {
////                Toast.makeText(this,"Ho gyaa", Toast.LENGTH_LONG).show()
//                pd.dismiss()
//                imageref.downloadUrl.addOnSuccessListener {
//                    profilepic = it.toString()
//                    Log.d("Sanjj",profilepic)
//                    Toast.makeText(this,"Image Uploaded", Toast.LENGTH_LONG).show()
////                var hogaya:String="hogaya"
//                }
//            }
//                    .addOnFailureListener {
//                        pd.dismiss()
//                        Toast.makeText(this,"Try Uploading Image Again", Toast.LENGTH_LONG).show()
//                    }
        }
       if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
//          imageView.visibility=
            var result=CropImage.getActivityResult(data)
            if (resultCode==Activity.RESULT_OK)
            {
                var resulturi=result.uri
                var pd=ProgressDialog(this)
                pd.setTitle("Saving Image To Database")
                pd.show()
                imageView.setImageURI(resulturi)
//                drawable = resources.getDrawable(R.drawable.demo_image)

//                bitmap1 = (drawable as BitmapDrawable).bitmap
//                var bitmap=  Bitmap bitmap = MediaStore.Images.Media.getBitmap(c.getContentResolver() , );
//                var bitmap=MediaStore.Images.Media.getBitmap(contentResolver, resulturi)
//                var byteArray = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArray)
//                var compressedbitmap=compressImage(bitmap)
//                var compresseduri=
////                var BYTE = ByteArrayOutputStream.toByteArray()
//              var byte=byteArray.toByteArray()
//                var compressedbitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size)
//               var compresseduri=getImageUri(this,compressedbitmap)
//                imageview.setImageBitmap(bitmap2)


//                var byteArray = ByteArrayOutputStream()
//                thumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
//                        byteArray)
//                var thumbByteArray: ByteArray
//                thumbByteArray = byteArray.toByteArray()

//           pd.progress=imageView.
//                Log.d("Compressed", compresseduri.toString())
                Log.d("Original", resulturi.toString())

//            val userId = UUID.randomUUID().toString()
                var userId=UUID.randomUUID().toString()
//            Log.d("Sanjaaa", userId)
                var imageref=   mStorageeref.child("My images").child("$userId.jpg")
                imageref.putFile(resulturi!!).addOnSuccessListener {
//                Toast.makeText(this,"Ho gyaa", Toast.LENGTH_LONG).show()
                    pd.dismiss()
                    imageref.downloadUrl.addOnSuccessListener {
                        profilepic = it.toString()
                        Log.d("Sanjj", profilepic)
                        Toast.makeText(this, "Image Uploaded", Toast.LENGTH_LONG).show()
//                var hogaya:String="hogaya"
                    }
                }
                        .addOnFailureListener {
                            pd.dismiss()
                            Toast.makeText(this, "Try Uploading Image Again", Toast.LENGTH_LONG).show()
                        }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


  private  fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }


//
//    private fun compressImage(image: Bitmap): Bitmap? {
//        val baos = ByteArrayOutputStream()
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos) //Compression quality, here 100 means no compression, the storage of compressed data to baos
//        var options = 90
//        while (baos.toByteArray().size / 1024 > 400) {  //Loop if compressed picture is greater than 400kb, than to compression
//            baos.reset() //Reset baos is empty baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos) //The compression options%, storing the compressed data to the baos
//            options -= 10 //Every time reduced by 10
//        }
//        val isBm = ByteArrayInputStream(baos.toByteArray()) //The storage of compressed data in the baos to ByteArrayInputStream
//        return BitmapFactory.decodeStream(isBm, null, null)
//    }
//    private fun getImageUri(context: Context, inImage: Bitmap): Uri? {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
//        val path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null)
//        return Uri.parse(path)
//    }


//    @Throws(IOException::class)
//    fun scaleImage(context: Context, photoUri: Uri?): Bitmap? {
//        var `is` = context.contentResolver.openInputStream(photoUri!!)
//        val dbo = BitmapFactory.Options()
//        dbo.inJustDecodeBounds = true
//        BitmapFactory.decodeStream(`is`, null, dbo)
//        `is`!!.close()
//        val rotatedWidth: Int
//        val rotatedHeight: Int
//        val orientation = getOrientation(context, photoUri)
//        if (orientation == 90 || orientation == 270) {
//            rotatedWidth = dbo.outHeight
//            rotatedHeight = dbo.outWidth
//        } else {
//            rotatedWidth = dbo.outWidth
//            rotatedHeight = dbo.outHeight
//        }
//        var srcBitmap: Bitmap?
//        `is` = context.contentResolver.openInputStream(photoUri)
//        if (rotatedWidth > 600 || rotatedHeight > 600) {
//            val widthRatio = rotatedWidth.toFloat() / 600 as Float
//            val heightRatio = rotatedHeight.toFloat() / 600 as Float
//            val maxRatio = Math.max(widthRatio, heightRatio)
//
//            // Create the bitmap from file
//            val options = BitmapFactory.Options()
//            options.inSampleSize = maxRatio.toInt()
//            srcBitmap = BitmapFactory.decodeStream(`is`, null, options)
//        } else {
//            srcBitmap = BitmapFactory.decodeStream(`is`)
//        }
//        `is`!!.close()
//
//        /*
//     * if the orientation is not 0 (or -1, which means we don't know), we
//     * have to do a rotation.
//     */if (orientation > 0) {
//            val matrix = Matrix()
//            matrix.postRotate(orientation)
//            srcBitmap = Bitmap.createBitmap(srcBitmap!!, 0, 0, srcBitmap.width,
//                    srcBitmap.height, matrix, true)
//        }
//        val type = context.contentResolver.getType(photoUri)
//        val baos = ByteArrayOutputStream()
//        if (type == "image/png") {
//            srcBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
//        } else if (type == "image/jpg" || type == "image/jpeg") {
//            srcBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        }
//        val bMapArray = baos.toByteArray()
//        baos.close()
//        return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.size)
//    }

//private fun Bitmap.compress(cacheDir: File, f_name: String): File? {
//    val f = File(cacheDir, "user$f_name.jpg")
//    f.createNewFile()
//    ByteArrayOutputStream().use { stream ->
//        compress(Bitmap.CompressFormat.JPEG, 70, stream)
//        val bArray = stream.toByteArray()
//        FileOutputStream(f).use { os -> os.write(bArray) }
//    }//stream
//    return f
//}


//    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
//        val bytes = ByteArrayOutputStream()
//        inImage.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
//        val path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null)
//        return Uri.parse(path)
//    }
}


