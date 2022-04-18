package com.example.tasks.util.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext

class NetworkCheck(
    private val connectivityManager: ConnectivityManager?,
    private val context: Context
) {

    fun doIfConnected(function: () -> Unit) {
        if (hasConnection()) {
            function()
        } else {
            Toast.makeText(context, "Sem conexão com a Internet.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasConnection(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network: Network = connectivityManager?.activeNetwork ?: return false
            val capabilities: NetworkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
        } else {
            val activeNetworkInfo = connectivityManager?.activeNetworkInfo
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
                        || activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE
            }
            return false
        }
    }
}