package com.example.eplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.eplan.ui.ActivityCard
import com.example.eplan.ui.BottomNavBar
import com.example.eplan.ui.SetupCalendar
import com.example.eplan.ui.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeComposer(navController: NavHostController) {

    val cards = remember { mutableListOf<List<String>>() }

    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
    cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))


    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) },
        topBar = { TopBar("Foglio ore") },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Create, contentDescription = "Aggiungi attività")
            }
        },
        content = {
            Column(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                SetupCalendar()
                LazyColumn() {
                    items(cards) { card ->
                        ActivityCard(
                            activityName = card[0],
                            activityDescription = card[1],
                            start = card[2],
                            end = card[3],
                            oreSpostamento = card[4],
                            km = card[5],
                            navController = navController
                        )
                    }
                }
            }
        }
    )
}