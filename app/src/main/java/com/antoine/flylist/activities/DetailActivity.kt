package com.antoine.flylist.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.antoine.flylist.R
import com.antoine.flylist.data.api.APIManager
import com.antoine.flylist.data.responses.Aircraft
import com.antoine.flylist.data.responses.Flight
import com.antoine.flylist.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.logging.Logger

class DetailActivity : AppCompatActivity() {

    private lateinit var flight: Flight

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        flight = intent.getParcelableExtra("flight")!!
        title = getString(R.string.flight_detail_title, flight.icao24.toString().uppercase())

        populateView()

        // TODO: Floating button
        findViewById<FloatingActionButton>(R.id.floating_button).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun populateView() {
        // Header
        flight.icao24?.let {
            APIManager.UTILITIES_API.airlineLogo(it.uppercase()).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful && response.body() != null && response.body()
                            .toString().lowercase() != "n/a"
                    ) {
                        Utils.loadImageFromURL(
                            this@DetailActivity,
                            response.body().toString(),
                            findViewById(R.id.airline)
                        )
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Logger.getGlobal().severe(t.stackTraceToString())
                }
            })
        }

        // Flight
        findViewById<TextView>(R.id.icao).text = flight.icao24.toString().uppercase()
        findViewById<TextView>(R.id.first_seen).text = Utils.epochToReadableDate(flight.firstSeen)
        flight.estDepartureAirport?.let {
            APIManager.UTILITIES_API.airportFromIcao(it).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful && response.body() != null) {
                        findViewById<TextView>(R.id.departure_airport).text = response.body()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Logger.getGlobal().severe(t.stackTraceToString())
                }
            })
        } ?: run {
            findViewById<TextView>(R.id.departure_airport).text = getString(R.string.n_a)
        }
        findViewById<TextView>(R.id.last_seen).text = Utils.epochToReadableDate(flight.lastSeen)
        flight.estArrivalAirport?.let {
            APIManager.UTILITIES_API.airportFromIcao(it).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful && response.body() != null) {
                        findViewById<TextView>(R.id.arrival_airport).text = response.body()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Logger.getGlobal().severe(t.stackTraceToString())
                }
            })
        } ?: run {
            findViewById<TextView>(R.id.arrival_airport).text = getString(R.string.n_a)
        }
        findViewById<TextView>(R.id.callsign).text = flight.callsign

        // Plane
        flight.icao24?.let {
            APIManager.AIRCRAFT_API.aircraftFromIcao(it).enqueue(object : Callback<Aircraft> {
                override fun onResponse(call: Call<Aircraft>, response: Response<Aircraft>) {
                    if (response.isSuccessful && response.body() != null) {
                        findViewById<TextView>(R.id.modes).text = it.uppercase()
                        findViewById<TextView>(R.id.manufacturer).text =
                            response.body()!!.Manufacturer
                        findViewById<TextView>(R.id.plane_type).text = response.body()!!.Type
                        findViewById<TextView>(R.id.plane_icao).text =
                            response.body()!!.ICAOTypeCode
                        findViewById<TextView>(R.id.owners).text =
                            response.body()!!.RegisteredOwners
                        findViewById<TextView>(R.id.flag_code).text =
                            response.body()!!.OperatorFlagCode
                    }
                }

                override fun onFailure(call: Call<Aircraft>, t: Throwable) {
                    Logger.getGlobal().severe(t.stackTraceToString())
                }
            })
        } ?: run {
            findViewById<TextView>(R.id.modes).text = getString(R.string.n_a)
            findViewById<TextView>(R.id.manufacturer).text = getString(R.string.n_a)
            findViewById<TextView>(R.id.plane_type).text = getString(R.string.n_a)
            findViewById<TextView>(R.id.plane_icao).text = getString(R.string.n_a)
            findViewById<TextView>(R.id.owners).text = getString(R.string.n_a)
            findViewById<TextView>(R.id.flag_code).text = getString(R.string.n_a)
        }
    }
}
