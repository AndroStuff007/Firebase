package com.dktechnology.firebase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.jar.Manifest


class AllUser : AppCompatActivity() {

    lateinit var rv : RecyclerView
    lateinit var grpchat : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_user)

        rv = findViewById(R.id.ContactrecycleView)
        grpchat = findViewById(R.id.grpchat)
        rv.layoutManager = LinearLayoutManager(this)

        grpchat.setOnClickListener {
            val intent = Intent(this,recycleViewActivity ::class.java)
            startActivity(intent)
        }


        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, Array(1){android.Manifest.permission.READ_CONTACTS},111)
        }else{
            readContact()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            readContact()
        }
    }

    @SuppressLint("Range")
    private fun readContact() {



        val contactList : MutableList<ContactData> = ArrayList()
        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
        while (contacts?.moveToNext() == true) {
            val n = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val no = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = ContactData()
            obj.name = n
            obj.number = no
            val photo_uri = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
            if(photo_uri != null){
                obj.image = MediaStore.Images.Media.getBitmap(contentResolver,Uri.parse(photo_uri))
            }
            contactList.add(obj)
        }

        rv.adapter = ContactAdapter(contactList,applicationContext)
        contacts?.close()


    }
}

