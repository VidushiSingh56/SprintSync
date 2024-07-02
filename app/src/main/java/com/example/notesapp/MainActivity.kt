// src/main/java/com/example/yourapp/MainActivity.kt
package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.notesapp.ui.theme.NotesAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
          NotesAppTheme{
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    noteApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun noteApp() {
    var notes by remember { mutableStateOf(listOf<String>()) }
    var showDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var newNote by remember { mutableStateOf(TextFieldValue("")) }
    var editingNoteIndex by remember { mutableStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notes App") },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                LazyVerticalGrid(columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)) {
                    items(notes.size) 
                    { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp).height(190.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth().padding(start = 10.dp, top = 10.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = notes[index],
                                    modifier = Modifier.weight(1f)
                                )
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End){
                                    IconButton(onClick = {
                                        editingNoteIndex = index
                                        newNote = TextFieldValue(notes[index])
                                        showEditDialog = true
                                    }) {
                                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Note")
                                    }
                                    IconButton(onClick = {
                                        notes = notes.toMutableList().also { it.removeAt(index) }
                                    }) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Note")
                                    }
                                }

                            }
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                notes = notes + newNote.text
                                newNote = TextFieldValue("")
                                showDialog = false
                            }
                        ) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    text = {
                        Column {
                            Text("Add a new note")
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = newNote,
                                onValueChange = { newNote = it }
                            )
                        }
                    }
                )
            }

            if (showEditDialog) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                notes = notes.toMutableList().also { it[editingNoteIndex] = newNote.text }
                                newNote = TextFieldValue("")
                                showEditDialog = false
                                editingNoteIndex = -1
                            }
                        ) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEditDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    text = {
                        Column {
                            Text("Edit note")
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = newNote,
                                onValueChange = { newNote = it }
                            )
                        }
                    }
                )
            }
        }
    )
}