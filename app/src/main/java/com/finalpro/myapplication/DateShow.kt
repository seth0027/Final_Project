package com.finalpro.myapplication

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_date__show.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class DateShow : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date__show)
        prog.visibility = View.VISIBLE

        val url = intent.getStringExtra("url")

        val t = Tas()
        t.execute(url)

        hdurl.setTextColor(Color.BLUE)
        hdurl.setOnClickListener { onClick() }
        img.setOnClickListener { onClick() }

        save.setOnClickListener {

            val db=SQL(this).writableDatabase

//            list.forEach{
//                if(it.date==date.text.toString()){
//                    Toast.makeText(this,"Already exist",Toast.LENGTH_LONG).show()
//                }
//                else{
//                    val cv=ContentValues()
//                    cv.put("da",date.text.toString())
//                    cv.put("explanation",explanation.text.toString())
//                    cv.put("hdurl",hdurl.text.toString())
//                    cv.put("url",url)
//                    cv.put("title",tit.text.toString())
//                    db.insert("Lit",null,cv)
//
//                    Toast.makeText(this,"Added to List",Toast.LENGTH_LONG).show()
//
//
//                }
//            }
            val c=db.rawQuery("Select * from Lit where da=?", arrayOf(date.text.toString()))

            if(c.count>0){
                Toast.makeText(this,"Already exist",Toast.LENGTH_LONG).show()
            }
            else{
                val cv=ContentValues()
                    cv.put("da",date.text.toString())
                    cv.put("explanation",explanation.text.toString())
                    cv.put("hdurl",hdurl.text.toString())
                    cv.put("url",url)
                    cv.put("title",tit.text.toString())
                    db.insert("Lit",null,cv)

                    Toast.makeText(this,"Added to List",Toast.LENGTH_LONG).show()

            }








        }


    }



    private fun onClick() {
        val url = hdurl.text.toString()
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }
//    inner class SQ(ctx:Context) : SQLiteOpenHelper(ctx,"NASA",null,1){
//        override fun onCreate(db: SQLiteDatabase?) {
//        db.execSQL("CREATE TABLE List(_id Integer PRIMARY KEY AUTOINCREMENT, url text, hdurl text,)")
//        }
//
//        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//
//        }
//
//    }
    inner class SQL(ctx:Context) : SQLiteOpenHelper(ctx,"Nasa",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("Create table Lit(_id INTEGER PRIMARY KEY AUTOINCREMENT, da text, explanation text, hdurl text, url text,title text) ")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
                 db?.execSQL("DROP TABLE IF EXISTS Lit")
        onCreate(db)
    }

}


    inner class Tas : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg params: String?): String {

            val url = URL(params[0])
            val urlConnection = url.openConnection() as HttpURLConnection
            val response = urlConnection.inputStream

            val reader = BufferedReader(InputStreamReader(response, "UTF-8"), 8)
            val sb = StringBuilder()
            var line: String = reader.readLine()
            while (line != null) {
                sb.append(line + "\n")
                try {
                    line = reader.readLine()
                } catch (e: Exception) {
                    break
                }
            }







            return sb.toString()
        }

        override fun onProgressUpdate(vararg values: Int?) {
//            super.onProgressUpdate(*values)
        }

        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
            val jObj = JSONObject(result)


            date.text = jObj.getString("date")
            explanation.text = jObj.getString("explanation")
//            url.text = jObj.getString("url")
            hdurl.text = jObj.getString("hdurl")
            tit.text = jObj.getString("title")
            Picasso.get().load(jObj.getString("hdurl")).into(img)

            img.visibility = View.VISIBLE




            prog.visibility = View.INVISIBLE

        }
    }
}


