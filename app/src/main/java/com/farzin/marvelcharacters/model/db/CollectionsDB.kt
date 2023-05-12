package com.farzin.marvelcharacters.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DBCharacters::class,DBNote::class], version = 1, exportSchema = false)
abstract class CollectionsDB : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun noteDao() : NoteDao
}