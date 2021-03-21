package com.antoine.flylist

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoine.flylist.data.AircraftAPI
import com.antoine.flylist.data.Flight
import com.antoine.flylist.data.FlightAPI
import com.antoine.flylist.io.CheckNetwork
import com.antoine.flylist.list.FlightsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {

    private lateinit var flightAPI: FlightAPI
    private lateinit var aircraftAPI: AircraftAPI
    private val flightsAdapter = FlightsAdapter(arrayOf())
    private lateinit var lastCall: Call<List<Flight>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Views hiding
        findViewById<ProgressBar>(R.id.loading_circle).visibility = View.GONE
        findViewById<TextView>(R.id.loading_text).visibility = View.GONE
        findViewById<TextView>(R.id.error_label).visibility = View.GONE
        findViewById<TextView>(R.id.no_connection_label).visibility = View.GONE

        findViewById<FloatingActionButton>(R.id.floating_button).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // Network
        if (Build.VERSION.SDK_INT >= 24) {
            CheckNetwork(applicationContext).registerNetworkCallback()
        }

        // APIs section
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = flightsAdapter
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
        }

        flightAPI = Retrofit.Builder()
            .baseUrl("https://opensky-network.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlightAPI::class.java)

        aircraftAPI = Retrofit.Builder()
            .baseUrl("https://api.joshdouch.me/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AircraftAPI::class.java)

        updateRecyclerViewWithAPICall(flightAPI.allFlights(1517227200, 1517229200))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                true
            }
            R.id.action_refresh -> {
                updateRecyclerViewWithAPICall(lastCall)
                true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateRecyclerViewWithAPICall(call: Call<List<Flight>>) {
        if (!this::lastCall.isInitialized || call != lastCall) lastCall = call
        // Empty Recycler View
        if (!flightsAdapter.isEmpty()) flightsAdapter.updateList(arrayOf())
        // Check for connection
        val errorText = findViewById<TextView>(R.id.error_label)
        errorText.visibility = View.GONE
        val noInternet = findViewById<TextView>(R.id.no_connection_label)
        if ((Build.VERSION.SDK_INT >= 24 && CheckNetwork.hasConnection) || (Build.VERSION.SDK_INT < 24 && (applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager).activeNetworkInfo?.isConnected == true)
        ) {
            noInternet.visibility = View.GONE
        } else {
            noInternet.visibility = View.VISIBLE
            return
        }
        // Managing UI
        val circle = findViewById<ProgressBar>(R.id.loading_circle)
        circle.visibility = View.VISIBLE
        val circleText = findViewById<TextView>(R.id.loading_text)
        circleText.visibility = View.VISIBLE
        // Debug log
        Logger.getGlobal().info("Call URL: " + call.request().url().toString())
        // Calling
        call.clone().enqueue(object : Callback<List<Flight>> {
            override fun onResponse(call: Call<List<Flight>>, response: Response<List<Flight>>) {
                if (response.isSuccessful && response.body() != null) {
                    circle.visibility = View.GONE
                    circleText.visibility = View.GONE
                    flightsAdapter.updateList(response.body()!!.toTypedArray())
                }
            }

            override fun onFailure(call: Call<List<Flight>>, t: Throwable) {
                circle.visibility = View.GONE
                circleText.visibility = View.GONE
                errorText.visibility = View.VISIBLE
                Toast.makeText(
                    this@MainActivity,
                    "Error: " + t.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
                Logger.getGlobal().severe("Error: " + t.localizedMessage)
            }
        })
    }

    companion object {
        fun epochToDate(epoch: String): String {
            return if (Build.VERSION.SDK_INT >= 26) {
                DateTimeFormatter.ISO_INSTANT.format(Instant.ofEpochSecond(epoch.toLong()))
            } else {
                SimpleDateFormat.getDateTimeInstance().format(Date(epoch.toLong() * 1000))
            }.replace("T", " ").replace("Z", " ").trim()
        }

        fun dateToEpoch(date: String): String {
            return SimpleDateFormat.getDateTimeInstance().parse(date)?.time.toString()
        }
    }
}
