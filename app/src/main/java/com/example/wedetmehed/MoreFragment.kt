package com.example.wedetmehed

import android.R.anim.slide_out_right
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.Navigation


class MoreFragment : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefManager=PrefManager(requireContext())
        view.findViewById<TextView>(R.id.Profileview).text=prefManager.getusername()
        val textView=view.findViewById<TextView>(R.id.selectedlanguage)
        if (prefManager.getlanguage()=="aa"){
            textView.text="Amharic"
        }else if (prefManager.getlanguage()=="en"){
            textView.text="English"
        }
        view.findViewById<CardView>(R.id.chooselanguagecard).setOnClickListener {
            val intent=Intent(activity,SelectLanguage_Fragment::class.java)
            activity?.overridePendingTransition(android.R.anim.slide_in_left,slide_out_right)
            startActivity(intent)
        }
        view.findViewById<CardView>(R.id.profilecardview).setOnClickListener {
            val intent=Intent(activity,ProfileFragment::class.java)
            activity?.overridePendingTransition(android.R.anim.slide_in_left,slide_out_right)
            startActivity(intent)

        }
    }
}