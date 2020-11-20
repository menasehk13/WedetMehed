@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.example.wedetmehed

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.util.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var fullname:EditText
    private lateinit var lastname:EditText
    private lateinit var email:EditText
    private lateinit var date:EditText
    private lateinit var imagepicker:ImageView
    private lateinit var register:Button
    private var PICKER:Int=300
    private  var imagepath:Uri?=null
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_register)
        fullname=findViewById(R.id.firstname)
        lastname=findViewById(R.id.lastname)
        email=findViewById(R.id.email)
        date=findViewById(R.id.dateofbirth)
        imagepicker=findViewById(R.id.profilepic)
        register=findViewById(R.id.register)
        date.setOnClickListener(){
            val c=Calendar.getInstance()
            c.add(Calendar.YEAR,-19)
           val yearof=c.get(Calendar.YEAR)
            val month=c.get(Calendar.MONTH)
            val day=c.get(Calendar.DAY_OF_MONTH)
            val datepicker=DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                date.setText("$i/$i2/$i3")
            },yearof,month,day)
            datepicker.datePicker.maxDate.and(c.timeInMillis)
            datepicker.show()
        }
        imagepicker.setOnClickListener(){
            val intent=Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.action=Intent.ACTION_GET_CONTENT
            startActivityForResult(intent,PICKER)
        }
        register.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==PICKER && data!=null&& data.data!=null) {
            imagepath = data.data!!
            val bitmap:Bitmap
             when {
                Build.VERSION.SDK_INT>=29 -> {
                   val imageDecoder=ImageDecoder.createSource(contentResolver,imagepath!!)
                    bitmap=ImageDecoder.decodeBitmap(imageDecoder)
                    imagepicker.setImageBitmap(bitmap)
                    Picasso.get().load(imagepath).transform(CropCircleTransformation()).fit().into(imagepicker)
                }
                else -> {
                    bitmap=MediaStore.Images.Media.getBitmap(contentResolver,imagepath)
                    imagepicker.setImageBitmap(bitmap)
                    Picasso.get().load(imagepath).transform(CropCircleTransformation()).fit().into(imagepicker)
                }
            }
        }


    }

    override fun onClick(p0: View?) {
       if(imagepath!=null){
           val firstname:String = fullname.text.toString()
           val lname=lastname.text.toString()
           val emailaddress:String=email.text.toString()
           val datepicked:String = date.text.toString()
           val phonenumber: String? =intent.getStringExtra("AddPhoneTODatabase")
           if (firstname.isEmpty()&&firstname.length<3){
               fullname.error="Please Enter Full Name"
               fullname.isFocusable.and(true)
               return
           }
           if(lname.isEmpty()&&lname.length<3){
               lastname.error="Please Enter Your LastName"
               fullname.isFocusable.and(true)
               return
           }
           if (emailaddress.isEmpty()){
               email.error="Please Add your Email"
               email.isFocusable.and(true)
               return
           }
           if (datepicked.isEmpty()){
               date.error="Please Enter Your BirthDate"
               date.isFocusable.and(true)
               return
           }
           val user=FirebaseAuth.getInstance()
           val uId:String=user.uid.toString()
           val collection=FirebaseFirestore.getInstance().collection("Users").document(uId)
           val storage=FirebaseStorage.getInstance().reference.child("UsersProfile").child("Image of User"+System.currentTimeMillis()+"."+getextenstion(imagepath!!))
           storage.putFile(imagepath!!).addOnCompleteListener(){ it ->
               val task=it.result.metadata?.reference?.downloadUrl
               task?.addOnSuccessListener {
                   val imagefile=it.toString()
                   val users= hashMapOf(
                       "FirstName" to firstname,
                       "LastName" to lname,
                       "EmailAddress" to emailaddress,
                       "BirthDate" to datepicked,
                       "PhoneNumber" to phonenumber,
                       "ImagePath" to imagefile
                   )
                   collection.set(users).addOnCompleteListener(){
                       if (it.isSuccessful){
                           val intnet=Intent(this,MainActivity::class.java)
                           val prefManager=PrefManager(applicationContext)
                           prefManager.saveUserName(firstname)
                           startActivity(intnet)
                           finish()
                       }
                   }
               }
           }
       }else{
           val firstname:String = fullname.text.toString()
           val lname=lastname.text.toString()

           val emailaddress:String=email.text.toString()
           val datepicked:String = date.text.toString()
           val phonenumber: String? =intent.getStringExtra("AddPhoneTODatabase")
           if (firstname.isEmpty()&&firstname.length<3){
               fullname.error="Please Enter Full Name"
               fullname.isFocusable.and(true)
               return
           }
           if(lname.isEmpty()&&lname.length<3){
               lastname.error="Please Enter Your LastName"
               fullname.isFocusable.and(true)
               return
           }
           if (emailaddress.isEmpty()){
               email.error="Please Add your Email"
               email.isFocusable.and(true)
               return
           }
           if (datepicked.isEmpty()){
               date.error="Please Enter Your BirthDate"
               date.isFocusable.and(true)
               return
           }
           val user=FirebaseAuth.getInstance()
           val uId:String=user.uid.toString()
           val collection=FirebaseFirestore.getInstance().collection("Users").document(uId)
           val users= hashMapOf(
               "FirstName" to firstname,
               "LastName" to lname
               ,"EmailAdress" to emailaddress,
               "Birthdate" to datepicked
               ,"PhoneNumber" to phonenumber
           )

           collection.set(users).addOnCompleteListener(){
               if (it.isSuccessful){
                   val intnet=Intent(this,MainActivity::class.java)
                   val prefManager=PrefManager(applicationContext)
                   prefManager.saveUserName(firstname)
                   startActivity(intnet)
                   finish()
               }
           }.addOnFailureListener(){
               Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
           }
       }
    }
    private fun noprofile(){
        val firstname:String = fullname.text.toString()
        val lname=lastname.text.toString()

        val emailaddress:String=email.text.toString()
        val datepicked:String = date.text.toString()
        val phonenumber: String? =intent.getStringExtra("AddPhoneTODatabase")
        if (firstname.isEmpty()&&firstname.length<3){
            fullname.error="Please Enter Full Name"
            fullname.isFocusable.and(true)
            return
        }
        if(lname.isEmpty()&&lname.length<3){
            lastname.error="Please Enter Your LastName"
            fullname.isFocusable.and(true)
            return
        }
        if (emailaddress.isEmpty()){
            email.error="Please Add your Email"
            email.isFocusable.and(true)
            return
        }
        if (datepicked.isEmpty()){
            date.error="Please Enter Your BirthDate"
            date.isFocusable.and(true)
            return
        }
        val user=FirebaseAuth.getInstance()
        val uId:String=user.uid.toString()
        val collection=FirebaseFirestore.getInstance().collection("Users").document(uId)
        val users= hashMapOf(
            "FirstName" to firstname,
            "LastName" to lname
            ,"EmailAdress" to emailaddress,
            "Birthdate" to datepicked
            ,"PhoneNumber" to phonenumber
        )

       collection.set(users).addOnCompleteListener(){
           if (it.isSuccessful){
               val intnet=Intent(this,MainActivity::class.java)
               val prefManager=PrefManager(applicationContext)
               prefManager.saveUserName(firstname)
               startActivity(intnet)
               finish()
           }
       }.addOnFailureListener(){
           Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
       }
    }
    private fun withprofile(){
        val firstname:String = fullname.text.toString()
        val lname=lastname.text.toString()
        val emailaddress:String=email.text.toString()
        val datepicked:String = date.text.toString()
        val phonenumber: String? =intent.getStringExtra("AddPhoneTODatabase")
        if (firstname.isEmpty()&&firstname.length<3){
            fullname.error="Please Enter Full Name"
            fullname.isFocusable.and(true)
            return
        }
        if(lname.isEmpty()&&lname.length<3){
            lastname.error="Please Enter Your LastName"
            fullname.isFocusable.and(true)
            return
        }
        if (emailaddress.isEmpty()){
            email.error="Please Add your Email"
            email.isFocusable.and(true)
            return
        }
        if (datepicked.isEmpty()){
            date.error="Please Enter Your BirthDate"
            date.isFocusable.and(true)
            return
        }
        val user=FirebaseAuth.getInstance()
        val uId:String=user.uid.toString()
        val collection=FirebaseFirestore.getInstance().collection("Users").document(uId)
        val storage=FirebaseStorage.getInstance().reference.child("UsersProfile").child("Image of User"+System.currentTimeMillis()+"."+getextenstion(imagepath!!))
        storage.putFile(imagepath!!).addOnCompleteListener(){ it ->
            val task=it.result.metadata?.reference?.downloadUrl
            task?.addOnSuccessListener {
                val imagefile=it.toString()
                val users= hashMapOf(
                    "FirstName" to firstname,
                    "LastName" to lname,
                    "EmailAddress" to emailaddress,
                    "BirthDate" to datepicked,
                     "PhoneNumber" to phonenumber,
                    "ImagePath" to imagefile
                )
                collection.set(users).addOnCompleteListener(){
                    if (it.isSuccessful){
                        val intnet=Intent(this,MainActivity::class.java)
                        val prefManager=PrefManager(applicationContext)
                        prefManager.saveUserName(firstname)
                        startActivity(intnet)
                        finish()
                    }
                }
            }
        }
    }
    private fun getextenstion(uri:Uri): String? {
        val cr:ContentResolver=contentResolver
        val mime:MimeTypeMap= MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(uri))
    }
}