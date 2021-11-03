package com.dktechnology.firebase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class AllUser : AppCompatActivity() {

    lateinit var rv : RecyclerView
    lateinit var grpchat : Button
    lateinit var logout : Button
    lateinit var auth: FirebaseAuth

     val contactList : MutableList<ContactData> = ArrayList()
     val RealUsers : MutableList<UserData> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_user)

        rv = findViewById(R.id.ContactrecycleView)
        grpchat = findViewById(R.id.grpchat)
        logout = findViewById(R.id.logout)
        rv.layoutManager = LinearLayoutManager(this)


        grpchat.setOnClickListener {
            val intent = Intent(this,recycleViewActivity ::class.java)
            startActivity(intent)
        }

        auth= FirebaseAuth.getInstance()

        val intent = intent
        //Toast.makeText(this,intent.getStringExtra("mobileNumber"),Toast.LENGTH_SHORT).show()


        logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }




        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this, Array(1){android.Manifest.permission.READ_CONTACTS},111)
        }else{
            readContact()
            CheckUserOnDatabase()
        }

    }

    private fun CheckUserOnDatabase() {
        val mydb  : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myref : DatabaseReference = mydb.getReference("RegisteredContacts")


        //Listener
        myref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                RealUsers.clear()

                //proceed if database contain data
                if (p0.exists()) {


                    for (msg in p0.children) {

                        for (i in 0 until contactList.size) {

                            val contacts = msg.getValue(UserData::class.java)
                            if (contacts!!.mobileNumber.equals(contactList[i].number)){
                                RealUsers.add(contacts)
                            }
                        }
                    }

                }

               rv.adapter = ContactAdapter(RealUsers,applicationContext)

            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun tt(msg : String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }




    @SuppressLint("Range")
    private fun readContact() {

        val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)
        while (contacts?.moveToNext() == true) {
            val n = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            var no = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = ContactData()
            obj.name = n
            no = no.replace("\\s".toRegex(),"")
            obj.number = no
            val photo_uri = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
            if(photo_uri != null){
                obj.image = MediaStore.Images.Media.getBitmap(contentResolver, Uri.parse(photo_uri))
            }

            contactList.add(obj)

        }

        //rv.adapter = ContactAdapter(contactList,applicationContext)
        contacts?.close()


    }







    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            readContact()
            CheckUserOnDatabase()
        }
    }
}

