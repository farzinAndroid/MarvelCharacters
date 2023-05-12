package com.farzin.marvelcharacters.connectivity_monitor

import kotlinx.coroutines.flow.Flow

interface ConnectivityObservable {

    fun observe() : Flow<Status>


}