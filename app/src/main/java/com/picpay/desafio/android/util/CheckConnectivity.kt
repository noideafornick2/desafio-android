package com.picpay.desafio.android.util

import android.app.Application
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import java.io.IOException

class CheckConnectivity(private val connectivityManager: ConnectivityManager?) :
    LiveData<Boolean>() {

    constructor(application: Application) : this(
        application.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    )

    constructor() : this(
        null
    )

    private val networkCallback = @RequiresApi(Build.VERSION_CODES.LOLLIPOP)

    object: ConnectivityManager.NetworkCallback(){

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            postValue(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        val builder= NetworkRequest.Builder()
        connectivityManager?.registerNetworkCallback(builder.build(),networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    @Throws(InterruptedException::class, IOException::class)
    fun isInternetAvailable(): Boolean {
        val command = "ping -c 0 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

    fun isInternetWorking(ctx : Context): Boolean {
        val manager = ctx.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            manager.getNetworkCapabilities(manager.activeNetwork)?.let {
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) ||
                        it.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            } ?: false
        else
            @Suppress("DEPRECATION")
            manager.activeNetworkInfo?.isConnectedOrConnecting == true
    }
}