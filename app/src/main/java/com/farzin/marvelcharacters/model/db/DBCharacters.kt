package com.farzin.marvelcharacters.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farzin.marvelcharacters.model.CharacterResult
import com.farzin.marvelcharacters.utils.convertListToString

@Entity(tableName = Constants.TABLE_NAME)
data class DBCharacters(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val apiId:Int?,
    val name:String?,
    val description : String?,
    val thumbnail:String?,
    val comics:String?
) {

    companion object{
        fun fromCharacter(character: CharacterResult) =
            DBCharacters(
                id = 0,
                apiId = character.id,
                name = character.name,
                description = character.description,
                thumbnail = character.thumbnail?.path + "." + character.thumbnail?.extension,
                comics = character.comics?.items?.mapNotNull { it.name }?.convertListToString() ?: "no comics"
            )
    }

}
