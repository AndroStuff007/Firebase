package com.dktechnology.firebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class initialization : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialization)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        auth=FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if(currentUser == null) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }else{
            startActivity(Intent(applicationContext, AllUser::class.java))
            finish()
        }
    }
}