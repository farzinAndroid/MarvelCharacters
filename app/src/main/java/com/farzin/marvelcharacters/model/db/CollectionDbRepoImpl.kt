package com.farzin.marvelcharacters.model.db

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CollectionDbRepoImpl(private val characterDao: CharacterDao,private val noteDao: NoteDao): CollectionDbRepo {


    override suspend fun getCharactersFromRepo(): Flow<List<DBCharacters>> {
        return characterDao.getCharacters()
    }

    override suspend fun getCharacterFromRepo(characterId: Int): Flow<DBCharacters> {
        return characterDao.getCharacter(characterId)
    }

    override suspend fun deleteCharacterFromRepo(character: DBCharacters) {
        characterDao.deleteCharacter(character)
    }

    override suspend fun updateCharacterFromRepo(character: DBCharacters) {
        characterDao.updateCharacter(character)
    }

    override suspend fun addCharacterFromRepo(character: DBCharacters) {
        characterDao.addCharacter(character)
    }








    override suspend fun getAllNotesFromRepo(): Flow<List<DBNote>> {
        return noteDao.getAllNotes()
    }

    override suspend fun getNotesAssociatedWithCharacterFromRepo(characterId: Int): Flow<List<DBNote>> {
        return noteDao.getNotesAssociatedWithCharacter(characterId)
    }

    override suspend fun addNotesFromRepo(note: DBNote) {
        return noteDao.addNotes(note)
    }

    override suspend fun updateNoteFromRepo(note: DBNote) {
        return noteDao.updateNote(note)
    }

    override suspend fun deleteNoteFromRepo(note: DBNote) {
        return noteDao.deleteNote(note)
    }

    override suspend fun deleteAllNotesAssociatedWithCharacterFromRepo(character: DBCharacters) {
        return noteDao.deleteAllNotesAssociatedWithCharacter(character.id)
    }


}