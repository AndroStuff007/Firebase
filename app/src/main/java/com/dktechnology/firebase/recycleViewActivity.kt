package com.dktechnology.firebase

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlin.collections.ArrayList

class recycleViewActivity : AppCompatActivity() {



    //Declaration
    lateinit var uMessage : EditText
    lateinit var send : Button
    private lateinit var dref : DatabaseReference
    private lateinit var userRecycleview : RecyclerView
    private lateinit var userArrayList: ArrayList<Data>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle_view)


        //initialization
        userRecycleview = findViewById(R.id.recycleView)
        userRecycleview.layoutManager = LinearLayoutManager(this)
        userRecycleview.setHasFixedSize(true)
        uMessage = findViewById(R.id.Umessage)
        send = findViewById(R.id.send)
        userArrayList = arrayListOf<Data>()


        //sending message
        send.setOnClickListener {
            sendData()
        }

        //retrieving messages from realtime database
        getUserData()

    }

        //method which retrieve data from database
    private fun getUserData() {


        dref = FirebaseDatabase.getInstance().getReference("Users")

            //Listener
        dref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                //clearing list for preventing duplication
                //userArrayList.clear()

                //proceed if database contain data
                if(p0.exists()){

                    for(msg in p0.children){
                        val messages = msg.getValue(Data::class.java)
                        userArrayList.add(messages!!)
                    }

                    //adapting data
                    userRecycleview.adapter = DataAdapter(userArrayList)
                    userRecycleview.smoothScrollToPosition(userArrayList.count() - 1)
                    userRecycleview.layoutManager!!.isItemPrefetchEnabled = true

                    //TODO("set focus of recycle view on last index")
                }

            }

            //if failed to retrieve data
            override fun onCancelled(p0: DatabaseError) {

                Toast.makeText(applicationContext, "PROBLEM OCCURS WHILE RETRIEVING DATA FROM DATABASE", Toast.LENGTH_LONG).show()

            }
        })
    }

    //method which send data to database

    private fun sendData(){

        val userMessage = uMessage.text.toString()


        if(userMessage.isEmpty()){
            return
        }

        //referring username from local storage
        val sharedPreferences : SharedPreferences = getSharedPreferences("data",Context.MODE_PRIVATE)
        val usrname = sharedPreferences.getString("username","N/A")!!


        //referring database from server side
        val mydb  : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myref : DatabaseReference = mydb.getReference("Users")



        //setting data to model class #DATA
        val data = Data(usrname,userMessage,System.currentTimeMillis())

        //TODO("have to use server time stamp")
        /*val timeStamp = ServerValue.TIMESTAMP
        val mutableList = timeStamp
        for(element in mutableList){
            println(element)
        }*/


        val key : String = myref.push().key.toString()
        myref.child(key).setValue(data)

        //clearing old message from textview
        uMessage.text = null


    }




}