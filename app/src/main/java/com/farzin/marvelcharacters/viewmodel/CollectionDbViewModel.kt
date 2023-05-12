package com.farzin.marvelcharacters.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farzin.marvelcharacters.connectivity_monitor.ConnectivityMonitor
import com.farzin.marvelcharacters.model.CharacterResult
import com.farzin.marvelcharacters.model.Note
import com.farzin.marvelcharacters.model.db.CollectionDbRepo
import com.farzin.marvelcharacters.model.db.DBCharacters
import com.farzin.marvelcharacters.model.db.DBNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDbViewModel @Inject constructor(
    private val repo: CollectionDbRepo,
    connectivityMonitor: ConnectivityMonitor
) : ViewModel() {

    val currentCharacter = MutableStateFlow<DBCharacters?>(null)
    val collection = MutableStateFlow<List<DBCharacters>>(emptyList())
    val notes = MutableStateFlow<List<DBNote>>(emptyList())
    val networkAvailable = connectivityMonitor


    init {
        getCollection()
        getNotes()
    }



    //DBCharacters
    private fun getCollection(){
        viewModelScope.launch {
            repo.getCharactersFromRepo().collect{
                collection.value = it
            }
        }
    }


    fun setCharacterId(characterId:Int?){
        characterId?.let {
            viewModelScope.launch {
                repo.getCharacterFromRepo(it).collect{
                    currentCharacter.value = it
                }
            }
        }
    }

    fun addCharacter(character: CharacterResult){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addCharacterFromRepo(DBCharacters.fromCharacter(character))
        }
    }

    fun deleteCharacter(character:DBCharacters){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteCharacterFromRepo(character)
            repo.deleteAllNotesAssociatedWithCharacterFromRepo(character)
        }
    }


    //DBNotes
    private fun getNotes(){
        viewModelScope.launch {
            repo.getAllNotesFromRepo().collect{
                notes.value = it
            }
        }
    }

    fun addNotes(note:Note){
        viewModelScope.launch(Dispatchers.IO) {
            repo.addNotesFromRepo(DBNote.fromNote(note))
        }
    }

    fun deleteNote(note: DBNote){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNoteFromRepo(note)
        }
    }


}