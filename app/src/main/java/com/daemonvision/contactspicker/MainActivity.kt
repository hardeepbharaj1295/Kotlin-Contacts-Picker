package com.daemonvision.contactspicker

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EnableRuntimePermission()

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent,1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1->{
                var phone: String?= null
                val uri = data!!.data
                val cr = contentResolver
                val cur = cr!!.query(uri,null,null,null,null)
                if(cur!!.moveToFirst())
                {
                    val str = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID)
                    )
                    if (cur.getInt(
                            cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER
                            )
                        ) > 0
                    ){
                        val pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            arrayOf(id), null
                        )
                        while (pCur!!.moveToNext()) {
                            phone = pCur.getString(
                                pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER
                                )
                            )
                            Log.i("Contactno", "Phone Number: $phone")
                        }
                        pCur.close()
                    }
                    Toast.makeText(this,str+phone,Toast.LENGTH_LONG).show()
                }
                cur.close()
                Log.e("Dta",data.toString())
            }
        }
    }

    fun EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.READ_CONTACTS
            )
        ) {
            Toast.makeText(this@MainActivity, "CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG)
                .show()
        } else {

            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                1
            )
        }
    }
}
