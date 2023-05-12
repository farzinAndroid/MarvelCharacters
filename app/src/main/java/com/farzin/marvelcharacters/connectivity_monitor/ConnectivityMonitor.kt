package com.farzin.marvelcharacters.connectivity_monitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ConnectivityMonitor private constructor(context: Context) : ConnectivityObservable{


    companion object : SingletonHolder<ConnectivityMonitor , Context>(::ConnectivityMonitor)

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager



    override fun observe(): Flow<Status>  = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback(){

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                launch { send(Status.Available) }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                launch { send(Status.Unavailable) }
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        if (connectivityManager.activeNetwork == null)
            launch { send(Status.Unavailable) }

        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.distinctUntilChanged()
}