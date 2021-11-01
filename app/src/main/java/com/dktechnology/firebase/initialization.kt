package com.dktechnology.firebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class initialization : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialization)

        val sharedPreferences : SharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val status = sharedPreferences.getString("status","nothing")!!

        if(status == "exist"){

            val intent = Intent(this,AllUser ::class.java)
            startActivity(intent)

        }else{

            val intent = Intent(this,MainActivity :: class.java)
            startActivity(intent)
        }
    }
}