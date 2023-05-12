package com.farzin.marvelcharacters.model.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

interface CollectionDbRepo {


    suspend fun getCharactersFromRepo(): Flow<List<DBCharacters>>

    suspend fun getCharacterFromRepo(characterId: Int): Flow<DBCharacters>

    suspend fun deleteCharacterFromRepo(character: DBCharacters)

    suspend fun updateCharacterFromRepo(character: DBCharacters)

    suspend fun addCharacterFromRepo(character: DBCharacters)



    suspend fun getAllNotesFromRepo(): Flow<List<DBNote>>

    suspend fun getNotesAssociatedWithCharacterFromRepo(characterId: Int): Flow<List<DBNote>>

    suspend fun addNotesFromRepo(note: DBNote)

    suspend fun updateNoteFromRepo(note: DBNote)

    suspend fun deleteNoteFromRepo(note: DBNote)

    suspend fun deleteAllNotesAssociatedWithCharacterFromRepo(character: DBCharacters)
}