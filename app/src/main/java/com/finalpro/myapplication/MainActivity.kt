package com.finalpro.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle


import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    //    private val main =MainActivity()
    private lateinit var toggle: ActionBarDrawerToggle
    private val my = Li()
    private lateinit var frag: Fragment
//    private val db = DateShow().SQL(this).writableDatabase

    val list = ArrayList<Item>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)









        toggle = ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name)
        drawer.addDrawerListener(toggle)

        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        nav.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.about -> {
                    AlertDialog.Builder(this).setNeutralButton("Know More") { _, _ ->
                        val url = "https://github.com/seth0027"
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    }.setPositiveButton("Okay") { _, _ -> }.setMessage("Allows the user to enter a date to retrieve an image from NASA’s web servers. A date picker object that allows the user to pick a given date•\tThe user can save various dates and images to the device for later viewing.The user can also delete images that have been saved to the device. ").setTitle("About").create().show()
                }
                R.id.showall -> {
                    quer("Select * from Lit")
                }
                R.id.clearall -> {
                    list.clear()
                    my.notifyDataSetChanged()
                    Toast.makeText(this, "Total ${list.size} elements in List ", Toast.LENGTH_LONG).show()
                }
                R.id.action_search -> {
                    Snackbar.make(li, "Type your date search at top ", Snackbar.LENGTH_SHORT).show()
                }


            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }




        li.adapter = my

        quer("Select * from Lit")








        li.setOnItemClickListener { _, _, position, _ ->

            val land = frame != null


            val b = Bundle()
            val item = list[position]
            b.putLong("id", item.id)
            b.putString("date", item.date)
            b.putString("explanation", item.explanation)
            b.putString("hdurl", item.hdurl)
            b.putString("picurl", item.picurl)
            b.putString("url", item.url)
            b.putString("title", item.title)

            if (land) {

                frag = InfoFragment()
                frag.arguments = b
                 supportFragmentManager
                        .beginTransaction().replace(R.id.frame, frag).commit()


            } else {
                val i = Intent(this, ShowActivity::class.java)
                i.putExtra("bundle", b)
                startActivity(i)
            }

//            val url = list[position].url
//            val i = Intent(this, DateShow::class.java)
//            i.putExtra("url", url)
//            startActivity(i)

        }

        li.setOnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(this).setPositiveButton("Delete") { _, _ ->
                val da = list[position].id.toString()

                val db = DateShow().SQL(this).writableDatabase
                db.delete("Lit", "_id=$da", null)
                list.removeAt(position)

                my.notifyDataSetChanged()

            }.setTitle(list[position].title).setMessage(list[position].explanation).setNegativeButton("Cancel") { _, _ -> }.create().show()


            true
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.men, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val sView = searchItem?.actionView as SearchView
        sView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                list.clear()

//                    "Select * from Lit Where da=$query Or explanation=$query Or hdurl=$query Or url=$query Or title=$query Or picurl=$query"
                quer("Select * from Lit where da='$query'")


                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })





        return super.onCreateOptionsMenu(menu)
    }

    private fun quer(query: String) {
        val db = DateShow().SQL(this).writableDatabase
        val c = db.rawQuery(query, null)

        while (c.moveToNext()) {
            val id = c.getLong(0)
            val date = c.getString(1)
            val explanation = c.getString(2)
            val hdurl = c.getString(3)
            val url = c.getString(4)
            val title = c.getString(5)
            val picurl = c.getString(6)
            list.add(Item(id, date, explanation, hdurl, url, title, picurl))


        }
        my.notifyDataSetChanged()
        Toast.makeText(this, "Total ${list.size} elements in List ", Toast.LENGTH_LONG).show()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true

        when (item.itemId) {
            R.id.about -> {
                AlertDialog.Builder(this).setNeutralButton("Know More") { _, _ ->
                    val url = "https://github.com/seth0027"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    startActivity(i)
                }.setPositiveButton("Okay") { _, _ -> }.setMessage("Allows the user to enter a date to retrieve an image from NASA’s web servers. A date picker object that allows the user to pick a given date•\tThe user can save various dates and images to the device for later viewing.The user can also delete images that have been saved to the device. ").setTitle("About").create().show()
            }
            R.id.showall -> {
                quer("Select * from Lit")
            }
            R.id.clearall -> {
                list.clear()
                my.notifyDataSetChanged()
                Toast.makeText(this, "Total ${list.size} elements in List ", Toast.LENGTH_LONG).show()
            }
        }
        return true
    }

    inner class Li : BaseAdapter() {


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            val vi = layoutInflater.inflate(R.layout.lay, parent, false)

            val date = vi.findViewById<TextView>(R.id.date)
            val tit = vi.findViewById<TextView>(R.id.tit)
            val imag= vi.findViewById<ImageView>(R.id.imag)

            date.text = list[position].date
            tit.text = list[position].title
            val ur = list[position].picurl
            Picasso.get().load(ur).resize(960, 720).into(imag)


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
            val mo = month



                val url = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=$year-$mo-$day"
                val i = Intent(activity, DateShow::class.java)
                i.putExtra("url", url)
                startActivity(i)

        }


    }



data class Item(val id: Long, val date: String, val explanation: String, val hdurl: String, val url: String, val title: String, val picurl: String)

