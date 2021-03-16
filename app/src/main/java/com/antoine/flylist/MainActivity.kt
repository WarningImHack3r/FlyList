package com.antoine.flylist

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antoine.flylist.data.Flight
import com.antoine.flylist.data.FlightAPI
import com.antoine.flylist.list.FlightsAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var api: FlightAPI
    private val flightsAdapter = FlightsAdapter(arrayOf())
    private lateinit var lastCall: Call<List<Flight>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.floating_button).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        // API section
        findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = flightsAdapter
        }

        api = Retrofit.Builder()
            .baseUrl("https://opensky-network.org/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlightAPI::class.java)

        updateRecyclerViewWithAPICall(api.allFlights(1517227200, 1517228500))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                updateRecyclerViewWithAPICall(lastCall)
                return true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateRecyclerViewWithAPICall(call: Call<List<Flight>>) {
        if (!flightsAdapter.isEmpty()) flightsAdapter.updateList(arrayOf())
        lastCall = call
        call.clone().enqueue(object : Callback<List<Flight>> {
            override fun onResponse(call: Call<List<Flight>>, response: Response<List<Flight>>) {
                if (response.isSuccessful && response.body() != null) {
                    flightsAdapter.updateList(response.body()!!.toTypedArray())
                }
            }

            override fun onFailure(call: Call<List<Flight>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: " + t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
