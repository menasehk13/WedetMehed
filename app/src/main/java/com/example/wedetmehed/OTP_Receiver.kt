package com.example.wedetmehed

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.widget.EditText

class OTP_Receiver : BroadcastReceiver() {
    fun setEditText(editText: EditText?) {
        Companion.editText2 = editText
    }
    override fun onReceive(context: Context, intent: Intent) {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (sms in messages) {
            val msg = sms.messageBody
            val address=sms.displayOriginatingAddress
            if (address=="CBE Birr"){
                val otp = msg.split(" ")[14]
                val otp2=otp.replace(".","")
                editText2!!.setText(otp2)
            }

        }
    }

    companion object {
        var editText2: EditText? = null
    }
}