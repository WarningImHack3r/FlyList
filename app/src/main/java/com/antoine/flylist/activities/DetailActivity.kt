package com.antoine.flylist.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.antoine.flylist.R

class DetailActivity : AppCompatActivity() {

    private lateinit var flightICAO: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        flightICAO = intent.getStringExtra("icao").toString()
        title = getString(R.string.flight_detail_title, flightICAO)
    }
}
