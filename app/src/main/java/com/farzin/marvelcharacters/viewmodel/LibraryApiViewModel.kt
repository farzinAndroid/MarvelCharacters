package com.farzin.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farzin.marvelcharacters.connectivity_monitor.ConnectivityMonitor
import com.farzin.marvelcharacters.model.api.MarvelApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryApiViewModel @Inject constructor(
    private val repo: MarvelApiRepository,
    connectivityMonitor: ConnectivityMonitor
) : ViewModel() {

    val result = repo.characters
    val queryText = MutableStateFlow<String>("")
    private val queryInput = Channel<String>(Channel.CONFLATED)
    val characterDetails = repo.characterDetails
    val networkAvailable = connectivityMonitor

    init {
        retrieveCharacters()
    }


    @OptIn(FlowPreview::class)
    private fun retrieveCharacters() {

        viewModelScope.launch(Dispatchers.IO) {
            queryInput.receiveAsFlow()
                .filter { validateQuery(it) }
                .debounce(1000)
                .collect {
                    repo.query(it)
                }
        }
    }

    private fun validateQuery(query: String): Boolean = query.length >= 2

    fun queryUpdate(input: String) {
        queryText.value = input
        queryInput.trySend(input)
    }

    fun getSingleCharacter(id: Int){
        repo.getSingleCharacter(id)
    }



}