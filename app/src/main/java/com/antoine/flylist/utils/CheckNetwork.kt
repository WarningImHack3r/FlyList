package com.antoine.flylist.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import com.antoine.flylist.FlyListApplication.Companion.context


open class CheckNetwork {
    open var hasConnection: Boolean? = null
        get() {
            if (Build.VERSION.SDK_INT < 24 || field == null) {
                @Suppress("DEPRECATION")
                return connectivityManager.activeNetworkInfo?.isConnected == true
            }
            return field
        }

    private var connectivityManager: ConnectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    companion object {
        val instance = CheckNetwork()
    }

    init {
        NetworkRequest.Builder()
        if (Build.VERSION.SDK_INT >= 24) {
            connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    hasConnection = true
                }

                override fun onLost(network: Network) {
                    hasConnection = false
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    hasConnection = false
                }
            })
        }
    }
}
