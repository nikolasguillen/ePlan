package com.example.eplan

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.eplan.ui.TopBar

const val name = "Nikolas"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountComposer(navController: NavHostController) {
    Scaffold(
        topBar = { MediumTopAppBar(title = { Text(text = "Ciao $name!") }) },
        content = {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = it.calculateBottomPadding()
                )
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    item() {
                        Text(
                            text = "Profilo",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.clickable { /*TODO*/ })
                    }
                    item {
                        Text(
                            text = "Impostazioni",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.clickable { /*TODO*/ })
                    }
                    item {
                        Text(
                            text = "Informazioni sull'app",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.clickable { /*TODO*/ })
                    }
                }
            }
        }
    )
}