package com.example.electrician

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.relocationRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.electrician.dataui.MainScreen
import com.example.electrician.dataui.dropDownMenu
import com.example.electrician.models.electrician
import com.example.electrician.ui.theme.ElectricianTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun InsertUI() {
    val context = LocalContext.current


    
    var check by remember {
        mutableStateOf<Boolean>(false)
    }

    var result by remember {
        mutableStateOf("")
    }
    var Imageuri by remember { mutableStateOf<Uri?>(null)}

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    var launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            Imageuri = uri

        }
    val list = listOf("Electrician", "Plumber", "Painter", "Carpenter")
    val expanded = remember { mutableStateOf(false) }
    val currentValue = remember { mutableStateOf("Category")}

    val database = Firebase.database
    val myRef = database.getReference(currentValue.value)

    var storageref  = Firebase.storage.reference
    

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
        Spacer(modifier = Modifier.height(20.dp))
        Text("Insert your data")
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .size(120.dp)
                .fillMaxHeight(0.4f)
                .clickable { launcher.launch("image/*") },
            shape = RoundedCornerShape(60.dp),
            elevation = 10.dp

        ) {
            Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){
                Image(painter = painterResource(id = R.drawable.person_search), contentDescription ="person image" ,
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp))

                Imageuri?.let {
                    if (Build.VERSION.SDK_INT < 28){
                        bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,it)
                    }else{
                        val Source = ImageDecoder.createSource(context.contentResolver, it)
                        bitmap = ImageDecoder.decodeBitmap(Source)
                    }

                    bitmap?.let{ btm->
                        Image(bitmap = btm.asImageBitmap(),
                            contentDescription = "profile photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize())
                    }
                }


            }

          //  Image(painter = painterResource(id = R.drawable.person_search), contentDescription ="person image" , modifier = Modifier.padding(10.dp))
//

        }
Spacer(modifier = Modifier.height(10.dp))
        //dropDownmenu
        Row(modifier = Modifier.fillMaxWidth().height(35.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Box(modifier = Modifier.fillMaxWidth()) {

                Row(modifier = Modifier
                    .clickable {
                        expanded.value = !expanded.value
                    }
                    .align(Alignment.Center)) {
                    Text(text = currentValue.value)
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)


                    DropdownMenu(expanded = expanded.value, onDismissRequest = {
                        expanded.value = false
                    }) {

                        list.forEach {

                            DropdownMenuItem(onClick = {
                                currentValue.value = it
                                expanded.value = false
                            }) {

                                Text(text = it)

                            }

                        }

                    }


                }

            }


        }


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
            OutlinedButton(onClick = {
                if (fname.isNotEmpty() and lname.isNotEmpty() and experience.isNotEmpty() ) {


                    val ref = storageref.child("$fname")
                    var downloadUri  = Imageuri

                    ref.putFile(Imageuri!!).continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        ref.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            downloadUri = task.result
                        } else {
                            downloadUri = null
                        }
                    }

                    var ele = electrician(fname, lname, experience.toInt(), ref.downloadUrl.toString()
                        )
                    myRef.child(fname).setValue(ele).addOnSuccessListener {
                        Toast.makeText(context, "Data inserted Successfully",Toast.LENGTH_SHORT).show()
                        fname = ""
                        lname = ""
                        experience = ""
                    }.addOnFailureListener{
                        Toast.makeText(context,"failed to connect",Toast.LENGTH_SHORT).show()
                    }



                } else {
                    Toast.makeText(context, "enter all fields", Toast.LENGTH_SHORT).show()
                }

            }, Modifier.padding(5.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.insert_icon),
                    contentDescription = "insert icon"
                )

            }
            //display button
            OutlinedButton(onClick = {

                                     val data = StringBuffer()
                myRef.get().addOnSuccessListener {
                    if(it.exists()){
                        it.children.forEach{
                            data.append(it.child("fname").value.toString() + " ")
                            data.append(it.child("lname").value.toString() + " ")
                            data.append("experience : "+ it.child("experience").value.toString() +"yrs" + "\n")
                        }
                        check = check.not()
                        result = data.toString()

                    }

                }.addOnFailureListener{

                }
            }, Modifier.padding(5.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.display_icon),
                    contentDescription = "Display icon"
                )
            }

            //update button
            OutlinedButton(onClick = {
                if (fname.isNotEmpty() and lname.isNotEmpty() and experience.isNotEmpty()) {
                    var ele = mapOf<String, String>(
                        "fname" to fname,
                        "lname" to lname,
                        "experience" to experience
                    )
                    myRef.child(fname).updateChildren(ele).addOnSuccessListener {
                        Toast.makeText(context, "Data updated Successfully",Toast.LENGTH_SHORT).show()
                        fname = ""
                        lname = ""
                        experience = ""
                    }.addOnFailureListener{
                        Toast.makeText(context,"failed to connect",Toast.LENGTH_SHORT).show()
                    }



                } else {
                    Toast.makeText(context, "enter all fields", Toast.LENGTH_SHORT).show()
                }

            }, Modifier.padding(5.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.update_icon),
                    contentDescription = "insert icon"
                )
            }

            //delete button
            OutlinedButton(onClick = {
                                     if(fname.isNotEmpty() ){
                                         myRef.child(fname).removeValue().addOnSuccessListener {
                                             Toast.makeText(context, "all Detail of Recoed Deleted", Toast.LENGTH_SHORT).show()
                                             fname = ""
                                             lname = ""
                                             experience = ""

                                         }.addOnFailureListener{
                                             Toast.makeText(context, "Error !! record not found", Toast.LENGTH_SHORT).show()
                                         }

                                     }else{
                                         Toast.makeText(context, "Enter name of Record to be Deleted", Toast.LENGTH_SHORT).show()
                                     }
            }, Modifier.padding(5.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.delete_icon),
                    contentDescription = "Delete icon"
                )
            }

            
         
        }
       
      AnimatedVisibility(visible = check, modifier = Modifier
          .fillMaxWidth()
          .clickable { check = false }) {
          Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
              Text(text = result)

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