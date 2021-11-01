package com.dktechnology.firebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {



    //Declaration
      lateinit var username : TextView
      lateinit var save: Button

    override fun onCreate(savedInstanceState: Bundle?) {


        //offline backup
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Initialization
        username = findViewById(R.id.uname)
        save = findViewById(R.id.save)


        //check weather user is new or old
        save.setOnClickListener {

            val intent = Intent(this,AllUser ::class.java)

            //If problem in username
            if(username.text.isEmpty()){
                username.error = "Enter valid username"
            }else{

                //proceed further If everything is fine

                    // store username locally
                val sharedPreferences : SharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE)
                val editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.apply{
                    putString("username",username.text.toString())
                    putString("status","exist")

                }.apply()

                //starting main chat activity #RECYCLE VIEW ACTIVITY
                startActivity(intent)

            }
        }
    }
}