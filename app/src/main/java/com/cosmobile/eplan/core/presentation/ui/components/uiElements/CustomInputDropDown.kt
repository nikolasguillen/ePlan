package com.cosmobile.eplan.core.presentation.ui.components.uiElements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun CustomInputDropDown(
    value: MutableState<String>,
    items: MutableList<String>,
    enabled: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val showDropDown = remember { mutableStateOf(false) }

    Box {
        OutlinedCard(shape = RoundedCornerShape(8.dp), modifier = modifier) {
            Row(
                modifier = modifier.clickable { if (enabled.value) showDropDown.value = true },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value.value,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                )
                IconButton(onClick = { if (enabled.value) showDropDown.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "dropdown"
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
        )
        DropdownMenu(
            expanded = showDropDown.value,
            onDismissRequest = { showDropDown.value = false }
        ) {
            items.forEach {
                Row(modifier = modifier
                    .wrapContentHeight()
                    .clickable {
                        value.value = it
                        showDropDown.value = false
                    }) {
                    Text(text = it, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}