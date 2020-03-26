package com.finalpro.myapplication

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.database.DataSetObserver

import android.os.Bundle

import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.finalpro.myapplication.DateShow.SQL
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

import java.util.*

class MainActivity : AppCompatActivity() {
    val list = ArrayList<Item>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
val db= DateShow().SQL(this).writableDatabase

        val c=db.rawQuery("Select * from Lit",null)

        while(c.moveToNext()){
            val id=c.getLong(0)
            val date=c.getString(1)
            val explanation=c.getString(2)
            val hdurl=c.getString(3)
            val url=c.getString(4)
            val title=c.getString(5)
            val picurl=c.getString(6)
            list.add(Item(id,date,explanation,hdurl,url,title,picurl))


        }
        Toast.makeText(this,"Total ${list.size} elements in List ",Toast.LENGTH_LONG).show()
         val my=Li()

            li.adapter=my

        my.notifyDataSetChanged()







    }
    inner class Li : BaseAdapter() {


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

           val vi= layoutInflater.inflate(R.layout.lay,parent,false)

            val date=vi.findViewById<TextView>(R.id.date)
            val tit=vi.findViewById<TextView>(R.id.tit)
            val imag:ImageView=vi.findViewById<ImageView>(R.id.imag)

            date.text=list[position].date
            tit.text=list[position].title
            val ur=list[position].picurl
            Picasso.get().load(ur).into(imag)

            return vi



        }




        override fun getItem(position: Int): Any {
            return list[position].date
        }

        override fun getItemId(position: Int): Long {
           return list[position].id
        }

        override fun getCount(): Int {
            return list.size
        }

    }

    fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

}

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity as Activity, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val mo=month+1
        val url = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=$year-$mo-$day"
        val i=Intent(activity,DateShow::class.java)
        i.putExtra("url",url)
        startActivity(i)

    }
}
data class Item(val id:Long,val date:String,val explanation :String,val hdurl:String,val url:String,val title:String,val picurl:String)

