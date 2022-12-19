package com.example.electrician

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.relocationRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.electrician.ui.theme.ElectricianTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.material.OutlinedTextField as OutlinedTextField1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElectricianTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    InsertUI()
                }
            }
        }
    }
}

@Composable
fun InsertUI() {
    val database = Firebase.database
    val myRef = database.getReference("Electrician")

    var fname by remember {
        mutableStateOf("")
    }
    var lname by remember {
        mutableStateOf("")
    }
    var experience by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Insert your data")

        TextField(value = fname, onValueChange = { fname = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            placeholder = {
                Text(text = "first name")
            })

        TextField(value = lname, onValueChange = { lname = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            placeholder = {
                Text(text = "last name")
            })

        TextField(value = experience, onValueChange = { experience = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            placeholder = {
                Text(text = "Years of experience")
            })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(onClick = { /*TODO*/ }, Modifier.padding(5.dp)) {
                Icon(painter = painterResource(id = R.drawable.insert_icon), contentDescription = "insert icon")

            }
            OutlinedButton(onClick = { /*TODO*/ }, Modifier.padding(5.dp)) {
                Icon(painter = painterResource(id = R.drawable.display_icon), contentDescription = "insert icon")


            }
            OutlinedButton(onClick = { /*TODO*/ }, Modifier.padding(5.dp)) {
                Icon(painter = painterResource(id = R.drawable.update_icon), contentDescription = "insert icon")


            }
            OutlinedButton(onClick = { /*TODO*/ }, Modifier.padding(5.dp)) {
                Icon(painter = painterResource(id = R.drawable.delete_icon), contentDescription = "insert icon")


            }
        }


    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ElectricianTheme {
        InsertUI()
    }
}