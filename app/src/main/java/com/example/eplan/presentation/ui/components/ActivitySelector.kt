package com.example.eplan.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.eplan.domain.model.Activity

@Composable
fun Activity(activities: List<Activity?>) {

    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(value = query, onValueChange = { query = it })
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(activities) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    it?.let {
                        RadioButton(selected = false, onClick = { /*TODO*/ })
                        Text(text = it.name)
                    }
                }
            }
        }
    }
}