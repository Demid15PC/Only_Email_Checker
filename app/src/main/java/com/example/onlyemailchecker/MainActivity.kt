package com.example.onlyemailchecker

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.example.onlyemailchecker.ui.theme.OnlyEmailCheckerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
       @OptIn(ExperimentalComposeUiApi::class)
       @SuppressLint("CoroutineCreationDuringComposition")
       override fun onCreate(savedInstanceState: Bundle?) {
              super.onCreate(savedInstanceState)
              setContent {
                     OnlyEmailCheckerTheme {
                            val scaffoldState = rememberScaffoldState()
                            val dialogState = remember { mutableStateOf(false) }

                            var email by remember { mutableStateOf("") }
                            var password by remember { mutableStateOf("") }

                            val keyboardController = LocalSoftwareKeyboardController.current

                            val scope = rememberCoroutineScope()

                            Scaffold(
                                   modifier = Modifier.fillMaxSize(),
                                   scaffoldState = scaffoldState
                            ) {
                                   if (error.value.isNotEmpty() && dialogState.value) {
                                          AlertDialog(
                                                 onDismissRequest = { dialogState.value = false },
                                                 title = { Text(text = "Error") },
                                                 text = { Text(text = error.value) },
                                                 confirmButton = {
                                                        Button(onClick = {
                                                               dialogState.value = false
                                                        }) {
                                                               Text(text = "Ok")
                                                        }
                                                 }
                                          )
                                   }
                                   Column(
                                          horizontalAlignment = Alignment.CenterHorizontally,
                                          verticalArrangement = Arrangement.Center,
                                          modifier = Modifier
                                                 .fillMaxSize()
                                                 .padding(horizontal = 30.dp)
                                   ) {
                                          OutlinedTextField(
                                                 value = email,
                                                 onValueChange = { email = it },
                                                 label = { Text(text = "Email") }
                                          )
                                          Spacer(modifier = Modifier.height(16.dp))
                                          OutlinedTextField(
                                                 value = password,
                                                 onValueChange = { password = it },
                                                 label = { Text(text = "Password") }
                                          )
                                          Spacer(modifier = Modifier.height(60.dp))
                                          Button(onClick = {
                                                 validate(email, password)
                                                 if (done.value) {
                                                        keyboardController?.hide()
                                                        scope.launch {
                                                               scaffoldState.snackbarHostState.showSnackbar(
                                                                      "zbst"
                                                               )
                                                        }
                                                 } else dialogState.value = true
                                          }) {
                                                 Text(text = "Click")
                                          }
                                   }
                            }
                     }
              }
       }
}

object EmailValidator {
       fun validate(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

object PasswordValidator {
       fun validate(password: String) = (password.length >= 8)
}

var error: MutableState<String> = mutableStateOf("")
var done: MutableState<Boolean> = mutableStateOf(false)

fun validate(email: String, password: String) {
       if (!EmailValidator.validate(email)) error.value = "Enter correct email"
       else if (!PasswordValidator.validate(password)) error.value = "Enter correct password"
       else done.value = true
}