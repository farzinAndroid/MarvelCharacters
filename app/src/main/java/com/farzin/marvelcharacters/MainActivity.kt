package com.farzin.marvelcharacters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.farzin.marvelcharacters.ui.theme.MarvelCharactersTheme
import com.farzin.marvelcharacters.view.CharacterScaffold
import com.farzin.marvelcharacters.view.CollectionScreen
import com.farzin.marvelcharacters.view.LibraryScreen
import com.farzin.marvelcharacters.viewmodel.CollectionDbViewModel
import com.farzin.marvelcharacters.viewmodel.LibraryApiViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route:String){

    object Collection: Destination(route = "collection")
    object Library: Destination(route = "library")
    object CharacterDetails: Destination(route = "character/{characterId}"){
        fun createRoute(characterId : Int?) = "character/$characterId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val lvm by viewModels<LibraryApiViewModel>()
    private val cvm by viewModels<CollectionDbViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelCharactersTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {

                    val navController = rememberNavController()

                    CharacterScaffold(navController = navController,lvm,cvm)


                }
            }
        }
    }
}


















