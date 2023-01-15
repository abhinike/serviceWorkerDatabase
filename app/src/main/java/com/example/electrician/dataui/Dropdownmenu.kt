package com.example.electrician.dataui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize


@Preview
@Composable
fun default() {
    dropDownMenu()

}

@Composable
fun dropDownMenu() {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Electrician", "Plumber", "Painter", "Carpenter")
    var selectedText by remember { mutableStateOf("Category") }

    var textfieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    Column(Modifier.padding(20.dp).clickable{expanded = !expanded}) {
       Row(
           Modifier
               .fillMaxWidth()
               .clickable {
                   expanded = !expanded

               },
       horizontalArrangement = Arrangement.SpaceBetween
           ) {
           Text(text = selectedText, modifier = Modifier.clickable{expanded = !expanded})
           Icon(icon, contentDescription = "dropdown icon")

       }

//        OutlinedTextField(
//            value = selectedText,
//            onValueChange = { selectedText = it },
//            modifier = Modifier.clickable{expanded = !expanded}
//                .fillMaxWidth()
//                .onGloballyPositioned { coordinates ->
//                    //This value is used to assign to the DropDown the same width
//                    textfieldSize = coordinates.size.toSize()
//                },
//            label = {Text("Label")},
//            trailingIcon = {
//                Icon(icon,"contentDescription"
//                    )
//            }
//        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current){textfieldSize.width.toDp()})
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }


}



@Composable
fun MainScreen() {

    val list = listOf("Electrician", "Plumber", "Painter", "Carpenter")
    val expanded = remember { mutableStateOf(false) }
    val currentValue = remember { mutableStateOf("Category") }



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



}