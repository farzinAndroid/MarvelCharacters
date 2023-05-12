package com.farzin.marvelcharacters.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.farzin.marvelcharacters.model.db.Constants.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("select * from $TABLE_NAME order by id asc")
    fun getCharacters(): Flow<List<DBCharacters>>

    @Query("select * from $TABLE_NAME where id = :characterId")
    fun getCharacter(characterId: Int): Flow<DBCharacters>

    @Delete
    fun deleteCharacter(character: DBCharacters)

    @Update
    fun updateCharacter(character: DBCharacters)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addCharacter(character: DBCharacters)
}