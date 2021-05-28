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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.antoine.flylist.R
import com.antoine.flylist.data.api.APIManager
import com.antoine.flylist.data.responses.Flight
import com.antoine.flylist.list.*
import com.antoine.flylist.utils.CheckNetwork
import com.antoine.flylist.utils.Utils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import java.util.*


class MainActivity : AppCompatActivity() {
    // detail, change lastCall, floating button, filter button

    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var viewModel: FlightViewModel
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

        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setColorSchemeResources(R.color.main_color, R.color.secondary_color)
        swipeContainer.setOnRefreshListener {
            updateRecyclerViewWithAPICall(lastCall)
        }

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
        viewModel = FlightViewModel(APIManager.FLIGHT_API.allFlights(epochPast, epochNow))
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

        // View model
        viewModel.setCall(call)
        viewModel.flightList.observe(this, {
            findViewById<ProgressBar>(R.id.loading_circle).isVisible = it is FetchLoading
            findViewById<TextView>(R.id.loading_text).isVisible = it is FetchLoading
            findViewById<TextView>(R.id.error_label).isVisible = it is FetchError
            if (it is FetchSuccess) flightsAdapter.updateList(it.flightList.toTypedArray())
        })

        // Swipe container
        if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
    }
}
