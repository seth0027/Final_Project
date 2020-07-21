package com.finalpro.myapplication

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db=SQL(this).writableDatabase
        val prefs=getSharedPreferences("login",Context.MODE_PRIVATE)
        user.setText(prefs.getString("username",""))
        pass.setText(prefs.getString("password",""))

        login.setOnClickListener{

            val username=user.text
            val password=pass.text



           val c= db.rawQuery("Select * from Login where username='$username' and password='$password'",null)

            if(c.count>0){
                val i= Intent(this,MainActivity::class.java)
                i.putExtra("username",username.toString())

                val prefs = getSharedPreferences("login", Context.MODE_PRIVATE)
                val edit=prefs.edit()
                edit.putString("username",username.toString())
                edit.putString("password",password.toString())
                edit.commit()

                startActivity(i)
            }

            else{
                AlertDialog.Builder(this).setTitle("Error").setMessage("Invalid Login").setPositiveButton("Try Again"){_,_->}.create().show()

            }




        }

        sign.setOnClickListener{
            val view=layoutInflater.inflate(R.layout.sign_up,null)


            AlertDialog.Builder(this).setTitle("Sign Up").setView(view).setPositiveButton("Sign up") { _, _ ->
                val username = view.findViewById<EditText>(R.id.user).text
                val password = view.findViewById<EditText>(R.id.pass).text
                val passwordVeri = view.findViewById<EditText>(R.id.passveri).text


                val c = db.rawQuery("Select * from Login where username='$username'", null)

                if (c.count > 0) {
                    Toast.makeText(this, "User with that name already exist", Toast.LENGTH_LONG).show()
                } else {
                    if (username.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "Username or password can not be empty", Toast.LENGTH_LONG).show()
                    } else {

                        if (password.toString()!=passwordVeri.toString()) {
                            Toast.makeText(this, "passwords do not match try again", Toast.LENGTH_LONG).show()
                        } else {
                            val cv = ContentValues()

                            cv.put("username", username.toString())
                            cv.put("password", password.toString())


                            db.insert("Login", null, cv)
                            Toast.makeText(this, "User added successfully", Toast.LENGTH_LONG).show()



                        }

                    }
                }
            }.setNeutralButton("Cancel") { _, _ -> }.create().show()
            }

        }








    inner class SQL(ctx:Context) : SQLiteOpenHelper(ctx,"Log",null,1){
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL("Create table Login(username text PRIMARY KEY , password text) ")

        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS Login")
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS Login")
            onCreate(db)
        }


    }
}
