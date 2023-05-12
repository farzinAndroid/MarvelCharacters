package com.farzin.marvelcharacters.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.farzin.marvelcharacters.model.Note

@Entity(tableName = Constants.NOTE_TABLE_NAME)
data class DBNote(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val characterId: Int,
    val title: String,
    val text: String,
){

    companion object{
        fun fromNote(note: Note) =
            DBNote(
                id = 0,
                characterId = note.characterId,
                title = note.title,
                text = note.text
            )

    }

}