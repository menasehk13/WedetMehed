package com.example.wedetmehed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.avatarfirst.avatargenlib.AvatarConstants
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProfileFragment : AppCompatActivity(){
    private lateinit var imageview:ImageView
    private lateinit var firstname:EditText
    private lateinit var lastname:EditText
    private lateinit var email:EditText
    private lateinit var birthday:EditText
    private lateinit var editprofile:Button
    private lateinit var saveedit:Button
    private lateinit var canecledit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_profile)
        imageview=findViewById(R.id.editprofileimage)
        firstname=findViewById(R.id.editfirstname)
        lastname=findViewById(R.id.editlastname)
        email=findViewById(R.id.editemail)
        birthday=findViewById(R.id.editbirthday)
        editprofile=findViewById(R.id.EditProfile)
        saveedit=findViewById(R.id.editsave)
        canecledit=findViewById(R.id.canceledit)
        val id:String=FirebaseAuth.getInstance().currentUser!!.uid
        val documentReference=FirebaseFirestore.getInstance().collection("Users").document(id)
        documentReference.get().addOnCompleteListener(){
            val documentSnapshot=it.result
            if (documentSnapshot.exists()){
                    firstname.setText(documentSnapshot.getString("FirstName"))
                    lastname.setText(documentSnapshot.getString("LastName"))
                    email.setText(documentSnapshot.getString("EmailAdress"))
                    birthday.setText(documentSnapshot.getString("Birthdate"))
                Picasso.get()
                    .load("https://brokenfortest")
                    .resize(50,50)
                    .placeholder(AvatarGenerator.avatarImage(this, 200, AvatarConstants.CIRCLE, firstname.text.toString()+"k"))
                    .into(imageview)
            }else{
                Picasso.get()
                    .load("https://brokenfortest")
                    .placeholder(AvatarGenerator.avatarImage(this, 200, AvatarConstants.CIRCLE, firstname.text.toString()+"k"))
                    .into(imageview)
            }
        }
      }
    }
