package com.example.eplan.presentation.ui.account

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onBackPressed: () -> Unit,
    navigateToCamera: () -> Unit,
    onListItemClick: (String) -> Unit,
    logout: () -> Unit
) {
    val state = viewModel.state
    val context = LocalContext.current

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
            TopAppBar(
                //TODO cambiare nome dinamicamente
                title = { stringResource(id = R.string.account) },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
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
                            /*.clickable {
                                when (cameraPermissionState.status) {
                                    PermissionStatus.Granted -> {
                                        navigateToCamera()
                                    }
                                    else -> {
                                        cameraPermissionState.launchPermissionRequest()
                                    }
                                }
                            }*/
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                        Text(
                            text = viewModel.username ?: stringResource(R.string.profilo),
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

            state.dialog?.let {
                AlertDialog(
                    onDismissRequest = it.onDismiss,
                    confirmButton = {
                        TextButton(onClick = it.onConfirm) {
                            Text(text = it.confirmText.asString(context))
                        }
                    },
                    dismissButton = {
                        it.dismissText?.let { dismissText ->
                            TextButton(onClick = it.onDismiss) {
                                Text(text = dismissText.asString(context))
                            }
                        }
                    },
                    title = {
                        it.title?.let { title ->
                            Text(text = title.asString(context))
                        }
                    },
                    text = {
                        Text(text = it.message.asString(context))
                    }
                )
            }
        }
    )
}