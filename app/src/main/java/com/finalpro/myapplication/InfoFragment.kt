package com.finalpro.myapplication

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_date__show.*

/**
 * A simple [Fragment] subclass.
 */
class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val vi= inflater.inflate(R.layout.fragment_info2, container, false)


        val date=vi.findViewById<TextView>(R.id.date)
        val explanation=vi.findViewById<TextView>(R.id.explanation)
        val url1=vi.findViewById<TextView>(R.id.url1)
        val hdurl=vi.findViewById<TextView>(R.id.hdurl)
        val tit=vi.findViewById<TextView>(R.id.tit)
        val img=vi.findViewById<ImageButton>(R.id.img)
        val save=vi.findViewById<Button>(R.id.save)




        val b=arguments
        val da= b?.getString("date")
        val id= b?.getLong("id")
        val exp= b?.getString("explanation")
        val hdu= b?.getString("hdurl")
        val picu= b?.getString("picurl")
        val u= b?.getString("url")
        val ti= b?.getString("title")



        date.text = da
        explanation.text = exp
        url1.text = picu
        hdurl.text = hdu
        tit.text = ti
        Picasso.get().load(picu).resize(960,720).into(img)

        hdurl.setTextColor(Color.BLUE)
        hdurl.setOnClickListener { onClick() }
        img.setOnClickListener { onClick() }

        img.visibility = View.VISIBLE
        url1.visibility=View.INVISIBLE
        save.visibility=View.INVISIBLE


        return vi
    }
    private fun onClick() {
        val url = hdurl.text.toString()
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

}
