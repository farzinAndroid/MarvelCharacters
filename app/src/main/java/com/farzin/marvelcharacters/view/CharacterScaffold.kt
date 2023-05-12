package com.farzin.marvelcharacters.view

import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.farzin.marvelcharacters.Destination
import com.farzin.marvelcharacters.viewmodel.CollectionDbViewModel
import com.farzin.marvelcharacters.viewmodel.LibraryApiViewModel

@Composable
fun CharacterScaffold(
    navController: NavHostController,
    lvm: LibraryApiViewModel,
    cvm: CollectionDbViewModel,
) {


    val scaffoldState = rememberScaffoldState()
    val ctx = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = { CharactersBottomNav(navHostController = navController) }
    ) { paddingValues ->

        NavHost(navController = navController, startDestination = Destination.Library.route) {

            composable(route = Destination.Library.route) {
                LibraryScreen(navController = navController,
                    vm = lvm,
                    paddingValues = paddingValues)
            }

            composable(route = Destination.Collection.route) {
                CollectionScreen(cvm,navController)
            }

            composable(route = Destination.CharacterDetails.route) { navBackStackEntry ->


                val id = navBackStackEntry.arguments?.getString("characterId")?.toIntOrNull()

                if (id == null) {
                    Toast.makeText(ctx, "Require Id", Toast.LENGTH_SHORT).show()
                } else {
                    lvm.getSingleCharacter(id)

                }


                CharacterDetailScreen(
                    lvm = lvm,
                    navController = navController,
                    paddingValues = paddingValues,
                    cvm = cvm
                )

            }

        }


    }

}