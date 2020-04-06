package com.finalpro.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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


        val name=intent.getStringExtra("username")
        user.text="Welcome $name"










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
                    }.setPositiveButton("Okay") { _, _ -> }.setMessage(R.string.about).setTitle(R.string.aboutH).create().show()
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
                    Snackbar.make(li, R.string.searchType, Snackbar.LENGTH_SHORT).show()
                }


            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }




        li.adapter = my


        quer("Select * from Lit where username='$name'")








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
            b.putString("username",intent.getStringExtra("username"))

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
                val itemSelected=list[position]

                val db = DateShow().SQL(this).writableDatabase
                val name=intent.getStringExtra("username")
                db.delete("Lit", "_id=$da and username=$name", null)
                list.removeAt(position)

                my.notifyDataSetChanged()

                Snackbar.make(button,"Total ${list.size} elements in List ",Snackbar.LENGTH_LONG).setAction("Undo"){
                    val cv = ContentValues()
                    cv.put("da", itemSelected.date)
                    cv.put("explanation", itemSelected.explanation)
                    cv.put("hdurl", itemSelected.hdurl)
                    cv.put("url", itemSelected.url)
                    cv.put("title", itemSelected.title)
                    cv.put("picurl", itemSelected.picurl)
                    cv.put("username",intent.getStringExtra("username"))
                    db.insert("Lit", null, cv)
                    list.add(position,itemSelected)
                    my.notifyDataSetChanged()
                    Toast.makeText(this,"Total ${list.size} elements in List",Toast.LENGTH_SHORT).show()

                }.show()



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
                val name=intent.getStringExtra("username")
//                    "Select * from Lit Where da=$query Or explanation=$query Or hdurl=$query Or url=$query Or title=$query Or picurl=$query"
                quer("Select * from Lit where da='$query' and username=$name")


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
                }.setPositiveButton("Okay") { _, _ -> }.setMessage(R.string.about).setTitle(R.string.aboutH).create().show()
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
            val mo = month + 1

            val yea = year.toString()
            lateinit var da: String
            if (day < 10) {
                da = String.format("%02d", day)
            } else {
                da = day.toString()
            }
            lateinit var mon: String
            if (mo < 10) {
                mon = String.format("%02d", mo)
            } else {
                mon = mo.toString()
            }
            val date = "$yea-$mon-$da"
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date2 = sdf.parse(date)
            val da3 = sdf.format(Date())
            val date3 = sdf.parse(da3)
            if (date2 > date3) {
                Toast.makeText(activity, "Select today's or previous date", Toast.LENGTH_LONG).show()
            } else {


                val db = DateShow().SQL(activity as Context).writableDatabase
                val c = db.rawQuery("Select * from Lit where da=?", arrayOf(date))
                if (c.count > 0) {
                    c.moveToNext()
                    val id = c.getLong(0)
                    val date = c.getString(1)
                    val explanation = c.getString(2)
                    val hdurl = c.getString(3)
                    val url = c.getString(4)
                    val title = c.getString(5)
                    val picurl = c.getString(6)
                    val username=activity?.intent?.getStringExtra("username")
                    val b = Bundle()

                    b.putLong("id", id)
                    b.putString("date", date)
                    b.putString("explanation", explanation)
                    b.putString("hdurl", hdurl)
                    b.putString("picurl", picurl)
                    b.putString("url", url)
                    b.putString("title", title)
                    b.putString("username",username)
                    val i = Intent(activity, ShowActivity::class.java)
                    i.putExtra("bundle", b)
                    startActivity(i)


                } else {
                    val url = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=$date"
                    val i = Intent(activity, DateShow::class.java)
                    i.putExtra("username",activity?.intent?.getStringExtra("username"))
                    i.putExtra("url", url)
                    startActivity(i)
                }
            }
        }

    }



data class Item(val id: Long, val date: String, val explanation: String, val hdurl: String, val url: String, val title: String, val picurl: String)

