package com.farzin.marvelcharacters.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.farzin.marvelcharacters.Destination
import com.farzin.marvelcharacters.utils.convertListToString
import com.farzin.marvelcharacters.viewmodel.CollectionDbViewModel
import com.farzin.marvelcharacters.viewmodel.LibraryApiViewModel

@Composable
fun CharacterDetailScreen(
    lvm: LibraryApiViewModel,
    navController: NavHostController,
    paddingValues: PaddingValues,
    cvm: CollectionDbViewModel,
) {

    val context = LocalContext.current
    val character = lvm.characterDetails.value
    val collection by cvm.collection.collectAsState()
    val inCollection = collection.map { it.apiId }.contains(character?.id)

    if (character == null) {
        navController.navigate(Destination.Library.route) {
            popUpTo(Destination.Library.route)
            launchSingleTop = true
        }
    }

    LaunchedEffect(key1 = Unit) {
        cvm.setCharacterId(character?.id)
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .padding(bottom = paddingValues.calculateBottomPadding())
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val imageUrl = character?.thumbnail?.path + "." + character?.thumbnail?.extension
        val name = character?.name ?: "No Name"
        val description = character?.description ?: "No Description"
        val comics =
            character?.comics?.items?.mapNotNull { it.name }?.convertListToString() ?: "No Comics"


        CharacterImage(
            url = imageUrl,
            modifier = Modifier
                .padding(4.dp)
                .width(200.dp)
        )




        Text(text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(4.dp))

        Text(text = comics,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(4.dp))

        Text(text = description,
            fontSize = 16.sp,
            modifier = Modifier.padding(4.dp))

        Button(onClick = {

                         if (!inCollection && character != null){
                             cvm.addCharacter(character)
                         }

        }, modifier = Modifier.padding(bottom = 40.dp)) {

            if (!inCollection){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text(text = "Add to collection")
                }
            } else{
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Text(text = "Added")
                }
            }




        }

    }

}