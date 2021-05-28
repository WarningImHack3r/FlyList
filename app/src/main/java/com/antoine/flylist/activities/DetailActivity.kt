package com.antoine.flylist.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.antoine.flylist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class DetailActivity : AppCompatActivity() {

    private lateinit var flightICAO: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        flightICAO = intent.getStringExtra("icao").toString()
        title = getString(R.string.flight_detail_title, flightICAO)

        // TODO: Floating button
        findViewById<FloatingActionButton>(R.id.floating_button).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}
