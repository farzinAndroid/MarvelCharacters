package com.farzin.marvelcharacters.di

import android.content.Context
import androidx.room.Room
import com.farzin.marvelcharacters.connectivity_monitor.ConnectivityMonitor
import com.farzin.marvelcharacters.model.api.ApiService
import com.farzin.marvelcharacters.model.api.MarvelApiRepository
import com.farzin.marvelcharacters.model.db.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object HiltModule {

    @Provides
    fun provideApiRepo() = MarvelApiRepository(ApiService.api)


    @Provides
    fun provideCollectionDb(@ApplicationContext context:Context) =
        Room.databaseBuilder(context,CollectionsDB::class.java,Constants.DB).build()


    @Provides
    fun provideCharacterDao(collectionsDB: CollectionsDB) = collectionsDB.characterDao()

    @Provides
    fun provideNoteDao(collectionsDB: CollectionsDB) = collectionsDB.noteDao()


    @Provides
    fun provideDBRepo(characterDao: CharacterDao,noteDao: NoteDao) : CollectionDbRepo =
        CollectionDbRepoImpl(characterDao,noteDao)

    @Provides
    fun connectivityMonitor(@ApplicationContext context: Context) =
        ConnectivityMonitor.getInstance(context)




}