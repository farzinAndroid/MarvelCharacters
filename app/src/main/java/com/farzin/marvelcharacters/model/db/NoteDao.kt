package com.farzin.marvelcharacters.model.db

import androidx.room.*
import com.farzin.marvelcharacters.model.db.Constants.NOTE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("select * from $NOTE_TABLE_NAME order by id")
    fun getAllNotes(): Flow<List<DBNote>>

    @Query("select * from $NOTE_TABLE_NAME where characterId = :characterId order by id asc")
    fun getNotesAssociatedWithCharacter(characterId: Int): Flow<List<DBNote>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNotes(note: DBNote)

    @Update
    fun updateNote(note: DBNote)

    @Delete
    fun deleteNote(note: DBNote)

    @Query("delete from $NOTE_TABLE_NAME where characterId = :characterId")
    fun deleteAllNotesAssociatedWithCharacter(characterId: Int)

}