package com.farzin.marvelcharacters.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.farzin.marvelcharacters.connectivity_monitor.Status
import com.farzin.marvelcharacters.model.Note
import com.farzin.marvelcharacters.model.db.DBNote
import com.farzin.marvelcharacters.ui.theme.GrayBackGround
import com.farzin.marvelcharacters.ui.theme.TransparentGray
import com.farzin.marvelcharacters.viewmodel.CollectionDbViewModel


@Composable
fun CollectionScreen(cvm: CollectionDbViewModel, navController: NavHostController) {

    val characterInCollection = cvm.collection.collectAsState()
    val expandedElement = remember { mutableStateOf(-1) }
    val notes = cvm.notes.collectAsState()
    val networkAvailable = cvm.networkAvailable.observe().collectAsState(Status.Available)

    Column(modifier = Modifier.fillMaxSize()) {

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

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(characterInCollection.value) { character ->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(4.dp)
                            .clickable {
                                if (expandedElement.value == character.id) {
                                    expandedElement.value = -1
                                } else {
                                    expandedElement.value = character.id
                                }
                            }
                    ) {

                        CharacterImage(
                            url = character.thumbnail,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxHeight(),
                            contentScale = ContentScale.FillHeight
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = character.name ?: "No name",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                maxLines = 2
                            )

                            Text(
                                text = character.comics ?: "",
                                fontStyle = FontStyle.Italic,
                            )
                        }

                        Column(
                            modifier = Modifier
                                .wrapContentWidth()
                                .fillMaxHeight()
                                .padding(4.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {

                            Icon(Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    cvm.deleteCharacter(character)
                                })

                            if (character.id == expandedElement.value)
                                Icon(Icons.Outlined.KeyboardArrowUp, contentDescription = null)
                            else
                                Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null)

                        }
                    }
                }

                if (character.id == expandedElement.value) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(TransparentGray)
                    ) {
                        val filteredNotes = notes.value.filter { notes ->
                            notes.characterId == character.id
                        }
                        NotesList(note = filteredNotes, cvm = cvm)
                        CreateNoteForm(characterId = character.id, cvm = cvm)
                    }
                }


                Divider(
                    color = Color.LightGray,
                    modifier = Modifier.padding(
                        top = 4.dp, bottom = 4.dp, start = 20.dp, end = 20.dp
                    ))
            }

        }


    }




}

@Composable
fun NotesList(note: List<DBNote>, cvm: CollectionDbViewModel) {
    for (notes in note) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(GrayBackGround)
                .padding(4.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = notes.title, fontWeight = FontWeight.Bold)
                Text(text = notes.text)
            }
            Icon(
                Icons.Outlined.Delete,
                contentDescription = null,
                modifier = Modifier.clickable {
                    cvm.deleteNote(notes)
                })
        }


    }
}

@Composable
fun CreateNoteForm(characterId: Int, cvm: CollectionDbViewModel) {
    var addNoteToElement by remember { mutableStateOf(-1) }
    var title by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }

    if (addNoteToElement == characterId){
        Column(
            modifier = Modifier
                .padding(4.dp)
                .background(GrayBackGround)
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(text = "Add note", fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Note Title") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(text = "Note Content") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Button(
                    onClick = {
                        val note = Note(characterId, title, text)
                        cvm.addNotes(note)
                        title = ""
                        text = ""
                        addNoteToElement = -1
                    }
                ) {
                    Icon(Icons.Default.Check, contentDescription =null )
                }
            }
        }
    }


    Button(onClick = { addNoteToElement = characterId }) {
        Icon(Icons.Default.Add, contentDescription = null)
    }
}

















