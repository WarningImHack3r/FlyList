package com.antoine.flylist.activities

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
import com.antoine.flylist.R
import com.antoine.flylist.data.api.APIManager
import com.antoine.flylist.data.responses.Flight
import com.antoine.flylist.list.FlightsAdapter
import com.antoine.flylist.utils.CheckNetwork
import com.antoine.flylist.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.logging.Logger


class MainActivity : AppCompatActivity() {
    // detail, change lastCall, floating button, filter button

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

        // TODO: Floating button
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

        val epochPast = Utils.dateToEpoch(Date(System.currentTimeMillis() - 3600 * 24000))
        val epochNow = Utils.dateToEpoch(Date(System.currentTimeMillis() - 3600 * 23000))
        updateRecyclerViewWithAPICall(APIManager.FLIGHT_API.allFlights(epochPast, epochNow))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                // TODO: implement this
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
        @Suppress("DEPRECATION")
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
                circle.visibility = View.GONE
                circleText.visibility = View.GONE
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        flightsAdapter.updateList(response.body()!!.toTypedArray())
                    } else {
                        errorText.visibility = View.VISIBLE
                        errorText.text = response.message()
                    }
                } else {
                    errorText.visibility = View.VISIBLE
                    errorText.text =
                        response.errorBody()!!.string().replace("[", "").replace("]", "")
                            .replace("\"", "")
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
                Logger.getGlobal().severe(t.stackTraceToString())
            }
        })
    }
}
