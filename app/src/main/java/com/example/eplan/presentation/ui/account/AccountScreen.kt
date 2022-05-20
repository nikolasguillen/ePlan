package com.example.eplan.presentation.ui.account

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState

const val name = "Nikolas"
// TODO prendere nome dal DB?

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onBackPressed: () -> Unit,
    navigateToCamera: () -> Unit,
    onListItemClick: (String) -> Unit,
    logout: () -> Unit
) {
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val accountItems = viewModel.accountItems

    LaunchedEffect(key1 = true) {
        viewModel.onTriggerEvent(AccountEvent.GetUriEvent)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
//              TODO cambiare nome dinamicamente
                title = { stringResource(id = R.string.account) },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.torna_indietro)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = logout) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = stringResource(id = R.string.logout)
                        )
                    }
                }
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier.padding(
                    bottom = it.calculateBottomPadding(),
                    top = it.calculateTopPadding()
                )
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = if (viewModel.imageUri != Uri.EMPTY) {
                                rememberAsyncImagePainter(model = viewModel.imageUri)
                            } else {
                                painterResource(id = R.drawable.ic_baseline_account_circle_24)
                            },
                            contentScale = ContentScale.FillWidth,
                            contentDescription = stringResource(R.string.profilo),
                            modifier = Modifier
                                .padding(
                                    start = MaterialTheme.spacing.medium,
                                    top = MaterialTheme.spacing.small,
                                    bottom = MaterialTheme.spacing.medium
                                )
                                .size(MaterialTheme.spacing.extraLarge)
                                .clip(CircleShape)
                                .clickable {
                                    when (cameraPermissionState.status) {
                                        PermissionStatus.Granted -> {
                                            navigateToCamera()
                                        }
                                        else -> {
                                            cameraPermissionState.launchPermissionRequest()
                                        }
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        Text(
                            text = stringResource(R.string.profilo),
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.padding(
                                end = MaterialTheme.spacing.medium,
                                top = MaterialTheme.spacing.small,
                                bottom = MaterialTheme.spacing.medium
                            )
                        )
                    }
                }
                items(accountItems) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onListItemClick(it.route) }) {
                        Icon(
                            imageVector = it.icon,
                            contentDescription = stringResource(it.nameResId),
                            modifier = Modifier.padding(
                                start = MaterialTheme.spacing.medium,
                                top = MaterialTheme.spacing.small,
                                bottom = MaterialTheme.spacing.small
                            )
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        Text(
                            text = stringResource(it.nameResId),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(
                                end = MaterialTheme.spacing.medium,
                                top = MaterialTheme.spacing.small,
                                bottom = MaterialTheme.spacing.small
                            )
                        )
                    }
                }
            }
        }
    )
}