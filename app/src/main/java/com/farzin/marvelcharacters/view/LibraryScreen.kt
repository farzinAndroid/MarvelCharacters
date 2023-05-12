package com.farzin.marvelcharacters.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.farzin.marvelcharacters.Destination
import com.farzin.marvelcharacters.connectivity_monitor.Status
import com.farzin.marvelcharacters.model.CharactersApiResponse
import com.farzin.marvelcharacters.model.api.NetworkResult
import com.farzin.marvelcharacters.viewmodel.LibraryApiViewModel


@Composable
fun LibraryScreen(
    vm: LibraryApiViewModel,
    paddingValues: PaddingValues,
    navController: NavHostController,
) {

    val result by vm.result.collectAsState()
    val text by vm.queryText.collectAsState()
    val networkAvailable = vm.networkAvailable.observe().collectAsState(Status.Available)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        if (networkAvailable.value == Status.Unavailable) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "network unavailable",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp))
            }

        }



        OutlinedTextField(
            value = text,
            onValueChange = vm::queryUpdate,
            maxLines = 1,
            label = { Text(text = "Type Characters") },
            placeholder = { Text(text = "Characters") }
        )


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            when (result) {
                is NetworkResult.Initial -> {
                    Text(text = "Search for a Character")
                }

                is NetworkResult.Loading -> {
                    CircularProgressIndicator()
                }

                is NetworkResult.Success -> {
                    showCharactersList(result, navController)
                }

                is NetworkResult.Error -> {
                    Text(text = "error : ${result.message}")
                }
            }

        }

    }


}

@Composable
fun showCharactersList(
    result: NetworkResult<CharactersApiResponse>,
    navController: NavHostController,
) {

    result.data?.data?.results?.let { characters ->
        LazyColumn(modifier = Modifier.background(Color.LightGray),
            verticalArrangement = Arrangement.Top) {
            result.data.attributionText?.let {
                item {
                    AttributionTextItem(text = it)
                }
            }

            items(characters) { characters ->

                val imageUrl = characters.thumbnail?.path + "." + characters.thumbnail?.extension
                val name = characters.name
                val desc = characters.description
                val context = LocalContext.current
                val id = characters.id

                Column(
                    modifier =
                    Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White)
                        .padding(4.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            if (id != null) {
                                navController.navigate(Destination.CharacterDetails.createRoute(id))
                            } else {
                                Toast
                                    .makeText(context,
                                        "Character does not exist",
                                        Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        CharacterImage(
                            url = imageUrl,
                            modifier = Modifier
                                .padding(4.dp)
                                .width(100.dp)
                        )


                        Column(modifier = Modifier.padding(4.dp)) {
                            Text(text = name ?: "", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(text = desc ?: "", maxLines = 4, fontSize = 14.sp)
                }

            }
        }
    }
}


@Composable
fun CharacterImage(
    url: String?,
    contentScale: ContentScale = ContentScale.FillWidth,
    modifier: Modifier,
) {

    AsyncImage(
        model = url,
        contentDescription = "",
        modifier = modifier,
        contentScale = contentScale)
}


@Composable
fun AttributionTextItem(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
        fontSize = 12.sp
    )
}
