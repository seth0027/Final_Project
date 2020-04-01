package com.finalpro.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        val b = intent.getBundleExtra("bundle")
        val frag = InfoFragment()
        frag.arguments = b
        val fm = supportFragmentManager
                .beginTransaction().replace(R.id.frame, frag).commit()

    }
}
