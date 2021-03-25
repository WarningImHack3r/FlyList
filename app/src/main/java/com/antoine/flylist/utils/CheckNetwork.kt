package com.antoine.flylist.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi


class CheckNetwork(private val context: Context) {
    companion object {
        var hasConnection = false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun registerNetworkCallback() {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            NetworkRequest.Builder()
            connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    hasConnection = true
                }

                override fun onLost(network: Network) {
                    hasConnection = false
                }
            })
            hasConnection = false
        } catch (e: Exception) {
            hasConnection = false
        }
    }
}
