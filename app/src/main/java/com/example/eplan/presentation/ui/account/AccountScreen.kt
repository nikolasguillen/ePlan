package com.example.eplan.presentation.ui.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eplan.R

const val name = "Nikolas"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onBackPressed: () -> Unit,
    toProfile: () -> Unit,
    toSettings: () -> Unit,
    toAppInfo: () -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(

//              TODO cambiare nome dinamicamente
                title = { Text(text = "Ciao $name!") },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.torna_indietro)
                        )
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(bottom = it.calculateBottomPadding())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { toProfile() }) {
                    Text(
                        text = stringResource(R.string.profilo),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { toSettings() }) {
                    Text(
                        text = stringResource(R.string.impostazioni),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { toAppInfo() }) {
                    Text(
                        text = stringResource(R.string.app_info),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    )
}