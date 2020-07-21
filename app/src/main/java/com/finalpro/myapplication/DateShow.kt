package com.finalpro.myapplication

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_date__show.*
import org.json.JSONException
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

            saveItem()


        }


    }

    private fun saveItem() {
        val url = intent.getStringExtra("url")

        val db = SQL(this).writableDatabase

        val username=intent.getStringExtra("username")

//
        val c = db.rawQuery("Select * from Lit where da=? and username=?", arrayOf(date.text.toString(),username))

        if (c.count > 0) {
            Toast.makeText(this, "Already exist", Toast.LENGTH_LONG).show()
        } else {
            val cv = ContentValues()
            cv.put("da", date.text.toString())
            cv.put("explanation", explanation.text.toString())
            cv.put("hdurl", hdurl.text.toString())
            cv.put("url", url)
            cv.put("title", tit.text.toString())
            cv.put("picurl", url1.text.toString())
            cv.put("username",username)
            db.insert("Lit", null, cv)
            val i=Intent(this, MainActivity::class.java)
            i.putExtra("username",username)
            startActivity(i)


        }

    }


    private fun loadWallpaper(url:String) {
        try {

            Log.d("Image",url)
//            val url = URL(url)
//            val image = BitmapFactory.decodeStream(url.openStream())

            val bm: Bitmap = (img.drawable as BitmapDrawable).bitmap

            val manager: WallpaperManager = WallpaperManager.getInstance(applicationContext)
            manager.setBitmap(bm)

            Toast.makeText(applicationContext, "Wallpaper set successfully", Toast.LENGTH_LONG).show()

        }
            catch (e: Exception) {
                Snackbar.make(img,"Some error occured try again ?",Snackbar.LENGTH_LONG).setAction("Try Again"){ loadWallpaper(url)}.show()
            }

    }




    private fun onClick() {
        val url = hdurl.text.toString()

        Log.d("Image",url)


        AlertDialog.Builder(this).setMessage("Do you want to set it as Wallpaper").setTitle("Action ?").setPositiveButton("Yes"){_,_->


               loadWallpaper(url)





        }.setNeutralButton("Open page"){_,_->

            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            Log.d("Image",url)
            startActivity(i)
        }.setNegativeButton("No"){_,_->}.create().show()

    }

    inner class SQL(ctx: Context) : SQLiteOpenHelper(ctx, "Nasa", null, 1) {
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL("Create table Lit(_id INTEGER PRIMARY KEY AUTOINCREMENT, da text, explanation text, hdurl text, url text,title text,picurl text,username text) ")

        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS Lit")
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS Lit")
            onCreate(db)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.men2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.sav -> {
                saveItem()

            }
        }
        return true

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
            val jObj= JSONObject(result)


            date.text = jObj.getString("date")
            explanation.text = jObj.getString("explanation")
            url1.text = jObj.getString("url")
            val pic=try{jObj.getString("hdurl")}catch (e:JSONException){jObj.getString("url")}
            hdurl.text =pic
            tit.text = jObj.getString("title")
            Picasso.get().load(pic).into(img)


                    //.resize(960, 720)

            img.visibility = View.VISIBLE
            save.visibility=View.VISIBLE
            url1.visibility = View.INVISIBLE




            prog.visibility = View.INVISIBLE

        }
    }
}


