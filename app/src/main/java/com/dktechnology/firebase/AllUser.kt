package com.dktechnology.firebase

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.database.Cursor
import android.util.Log
import android.widget.Toast


class AllUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_user)
        getContactList()
    }

    @SuppressLint("Range")
    private fun getContactList() {
        val cr = contentResolver
        val cur: Cursor? = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )


        if ((if (cur != null) cur.getCount() else 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name: String = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(
                        cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur: Cursor? = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur?.moveToNext() == true) {
                        val phoneNo: String = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )

                        Toast.makeText(applicationContext, name, Toast.LENGTH_LONG).show()

                        Log.i(TAG, "Name: $name")
                        Log.i(TAG, "Phone Number: $phoneNo")
                    }
                    pCur?.close()
                }
            }
        }
        if (cur != null) {
            cur.close()
        }
    }

}