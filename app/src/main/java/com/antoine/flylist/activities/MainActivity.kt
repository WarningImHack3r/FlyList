package com.antoine.flylist.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.antoine.flylist.fragments.ModalFragment
import com.antoine.flylist.list.*
import com.antoine.flylist.utils.CheckNetwork
import com.antoine.flylist.utils.Utils


class MainActivity : AppCompatActivity() {
    // TODO: detail async + mvvm, floating button

    lateinit var viewModel: FlightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        // Recycler View
        val flightsAdapter = FlightsAdapter(arrayOf())
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

        // View Model
        viewModel = FlightViewModel(
            APIManager.FLIGHT_API.allFlights(
                Utils.epochNowMinusHour(24),
                Utils.epochNowMinusHour(23)
            )
        )
        viewModel.registerObserver(this) {
            val hasConnection = CheckNetwork.instance.hasConnection!!
            findViewById<TextView>(R.id.no_connection_label).isVisible =
                it is FetchError && !hasConnection
            findViewById<ProgressBar>(R.id.loading_circle).isVisible =
                it is FetchLoading && hasConnection
            findViewById<TextView>(R.id.loading_text).isVisible =
                it is FetchLoading && hasConnection
            findViewById<TextView>(R.id.error_label).isVisible = it is FetchError && hasConnection
            if (it is FetchLoading && !flightsAdapter.isEmpty()) flightsAdapter.updateList(arrayOf())
            if (it is FetchSuccess) flightsAdapter.updateList(it.flightList.toTypedArray())
        }
        viewModel.call = viewModel.call

        // Swipe container
        val swipeContainer = findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setColorSchemeResources(
            R.color.main_color,
            R.color.secondary_color
        )
        swipeContainer.setOnRefreshListener {
            viewModel.call = viewModel.call
            swipeContainer.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                viewModel.call = viewModel.call
                true
            }

            R.id.action_filter -> {
                supportFragmentManager.beginTransaction().add(ModalFragment(), null)
                    .addToBackStack(null).commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
