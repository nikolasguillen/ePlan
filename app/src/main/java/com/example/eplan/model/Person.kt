package com.example.eplan.model

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class Person(private val name: String, private var isChecked: Boolean = false) {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PersonComposer() {
        val checkedState = remember { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = name)
            Checkbox(checked = checkedState.value, onCheckedChange = {
                checkedState.value = it
                isChecked = checkedState.value
            })
        }
    }

    fun getCheckedState(): Boolean {
        return isChecked
    }
}