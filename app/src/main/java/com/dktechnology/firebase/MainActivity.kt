package com.dktechnology.firebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.database.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {



    //Declaration
      lateinit var mobileNumber : EditText
      lateinit var getOTP: Button
      lateinit var enteredOTP : EditText
      lateinit var submitOTP: Button
    lateinit var userArrayList: ArrayList<UserData>



    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId:String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {


        //offline backup
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth=FirebaseAuth.getInstance()
        userArrayList = arrayListOf<UserData>()


        val currentUser = auth.currentUser
        if(currentUser != null) {
            startActivity(Intent(applicationContext, AllUser::class.java))
            finish()
        }


        //Initialization
        mobileNumber = findViewById(R.id.mnumber)
        getOTP = findViewById(R.id.getOTP)
        enteredOTP = findViewById(R.id.enteredOTP)
        submitOTP = findViewById(R.id.submitOTP)

        getOTP.setOnClickListener{
            login()
        }




        // Callback function for Phone Auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                CheckUserOnDatabase()
                OpenMainScreen()
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
            }
        }


        submitOTP.setOnClickListener {

                val otp=enteredOTP.text.toString().trim()
                if(!otp.isEmpty()){
                    val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                        storedVerificationId.toString(), otp)
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()
                }

        }
    }

    private fun CheckUserOnDatabase() {
        val mydb  : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myref : DatabaseReference = mydb.getReference("RegisteredContacts")

        //Listener
        myref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                val contacts = ArrayList<String>()


                //proceed if database contain data
                if(p0.exists()){

                    for(msg in p0.children){
                        val contacts = msg.getValue(UserData::class.java)
                        userArrayList.add(contacts!!)
                        }

                    }
                val data = UserData("+91"+mobileNumber.text.toString())
                ShowToast(data.mobileNumber)
                    if(userArrayList.contains(data)){

                        ShowToast("old user")
                    }else{ RegisterNumber()}
                }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun ShowToast(msg :String){

        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()

    }

    private fun OpenMainScreen(){

        val intent = Intent(this, AllUser::class.java)
// To pass any data to next activity
        intent.putExtra("mobileNumber", mobileNumber.text.toString())
// start your next activity
        startActivity(intent)
        finish()

    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    CheckUserOnDatabase()
                    val intent = Intent(this, AllUser::class.java)
// To pass any data to next activity
                    intent.putExtra("mobileNumber", mobileNumber.text.toString())
// start your next activity
                    startActivity(intent)
                    finish()
// ...
                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {

                        Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
                    }
                }
            }


    }

    private fun login() {
        val mn = mobileNumber.text
        var number=mn.toString().trim()

        if(!number.isEmpty()){
            number="+91"+number
            sendVerificationcode (number)

            mobileNumber.visibility = View.GONE
            getOTP.visibility = View.GONE

            enteredOTP.visibility = View.VISIBLE
            submitOTP.visibility = View.VISIBLE

        }else{
            Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
        }
    }

    private fun RegisterNumber(){

        //if(newUser){
        val mn = "+91"+ mobileNumber.text.toString().trim()
        val mydb  : FirebaseDatabase = FirebaseDatabase.getInstance()
        val myref : DatabaseReference = mydb.getReference("RegisteredContacts")

        val key : String = myref.push().key.toString()
        val userData = UserData(mn)
        myref.child(key).setValue(userData)
        //}

    }

    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
}