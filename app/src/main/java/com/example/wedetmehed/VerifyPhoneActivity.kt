package com.example.wedetmehed

import `in`.aabhasjindal.otptextview.OtpTextView
import android.annotation.SuppressLint
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.*
import com.google.firebase.firestore.FirebaseFirestore
import dmax.dialog.SpotsDialog
import java.util.concurrent.TimeUnit
import kotlin.math.log10

class VerifyPhoneActivity : AppCompatActivity() {
    private val auth=FirebaseAuth.getInstance()
    lateinit var verificationId:String
    lateinit var otpTextView: OtpTextView
    private lateinit var timer:TextView
    private lateinit var resend:Button
    private lateinit var  phonenum:String
    lateinit var verfiyButton:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_verify_phone)
        otpTextView=findViewById(R.id.otpview)
        timer=findViewById(R.id.timer)
        resend=findViewById(R.id.resend)
        verfiyButton=findViewById(R.id.verfiyphonenbutton)
       phonenum= intent.getStringExtra("Phone").toString()
        sendsmsmessage(phonenum)
         val alertDialog=SpotsDialog.Builder()
             .setContext(this)
             .setCancelable(false)
             .setMessage("Verfying Please Wait A Moment")
             .build()
        val countDownTimer=object :CountDownTimer(60000,1000){
            @SuppressLint("SetTextI18n")
            override fun onTick(p0: Long) {
                val minute=(p0/(1000*60))%60
                val second=(p0/1000)%60
                timer.text="$minute:$second"

            }

            override fun onFinish() {
                resend.visibility= View.VISIBLE

            }

        }.start()
        resend.setOnClickListener(){
            countDownTimer.start()
            resend.visibility=View.INVISIBLE
        }
            verfiyButton.setOnClickListener(){
                val code:String=otpTextView.otp.toString()
                if (code.isEmpty()){
                    otpTextView.showError()
                    return@setOnClickListener
                }
                alertDialog.show()
                VerifyCode(code)
            }
    }

    private fun sendsmsmessage(phonenum: String) {
            val options=PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+$phonenum")
                .setTimeout(120,TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callback)
                .build()
        verifyPhoneNumber(options)
    }
    private val callback=object : OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            val code=p0.smsCode.toString()
                VerifyCode(code)
                otpTextView.otp = code

        }

        override fun onCodeSent(p0: String, p1: ForceResendingToken) {
            super.onCodeSent(p0, p1)
            verificationId=p0
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()

        }
    }

    private fun VerifyCode(code: String) {
        val phonecredential=PhoneAuthProvider.getCredential(verificationId,code)
        signinwithcredential(phonecredential)
    }

    private fun signinwithcredential(p0: PhoneAuthCredential) {
          auth.signInWithCredential(p0).addOnCompleteListener(){ it ->
              if (it.isSuccessful){
                      val user=it.getResult().user
                  val userid=user?.uid.toString()
                  val collection=FirebaseFirestore.getInstance().collection("Users").document(userid)
                  collection.get().addOnSuccessListener(){
                      if (it.exists()){
                          val username=it.getString("FirstName").toString()
                          val prefManager=PrefManager(applicationContext)
                          prefManager.saveUserName(username)
                          Toast.makeText(this,"Already Have Account",Toast.LENGTH_LONG).show()
                          val intent=Intent(this,MainActivity::class.java)
                          startActivity(intent)
                          finish()
                      }else{Toast.makeText(this,"No Files Register",Toast.LENGTH_LONG).show()
                          val intent=Intent(this,RegisterActivity::class.java)
                          intent.putExtra("AddPhoneTODatabase",phonenum)
                          startActivity(intent)
                          finish()
                      }
                  }
              }
          }.addOnFailureListener(){
               Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
          }
    }
}