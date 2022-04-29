package com.example.eplan.presentation.ui.account

import android.media.ThumbnailUtils
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toIcon
import androidx.core.net.toFile
import coil.compose.rememberAsyncImagePainter
import com.example.eplan.R
import com.example.eplan.presentation.ui.camera.CameraView
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.spacing
import com.google.accompanist.permissions.*

const val name = "Nikolas"
// TODO prendere nome dal DB?

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onBackPressed: () -> Unit,
    navigateToCamera: () -> Unit,
    toProfile: () -> Unit,
    toSettings: () -> Unit,
    toAppInfo: () -> Unit,
    logout: () -> Unit
) {
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

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
                }
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(
                    bottom = it.calculateBottomPadding(),
                    top = it.calculateTopPadding()
                )
            ) {
                val rowModifier = Modifier.fillMaxWidth()
                val textModifier = Modifier.padding(
                    end = MaterialTheme.spacing.medium,
                    top = MaterialTheme.spacing.small,
                    bottom = MaterialTheme.spacing.small
                )
                val iconModifier = Modifier.padding(
                    start = MaterialTheme.spacing.medium,
                    top = MaterialTheme.spacing.small,
                    bottom = MaterialTheme.spacing.small
                )

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = rowModifier
                        .clickable { toProfile() }) {
                    Image(
                        painter = if (viewModel.imageUri != Uri.EMPTY) {
                            rememberAsyncImagePainter(model = viewModel.imageUri)
                        } else {
                            painterResource(id = R.drawable.ic_baseline_account_circle_24)
                        },
                        contentScale = ContentScale.FillWidth,
                        contentDescription = stringResource(R.string.profilo),
                        modifier = iconModifier
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
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = textModifier
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = rowModifier
                        .fillMaxWidth()
                        .clickable { toSettings() }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(R.string.impostazioni),
                        modifier = iconModifier
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Text(
                        text = stringResource(R.string.impostazioni),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = textModifier
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = rowModifier
                        .fillMaxWidth()
                        .clickable { toAppInfo() }) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = stringResource(R.string.app_info),
                        modifier = iconModifier
                    )
                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))
                    Text(
                        text = stringResource(R.string.app_info),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = textModifier
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = rowModifier
                        .clickable { logout() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_exit_to_app_24),
                        contentDescription = stringResource(R.string.logout),
                        modifier = iconModifier
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(R.string.logout),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = textModifier
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun FeatureThatRequiresCameraPermission(cameraPermissionState: PermissionState) {


    when (cameraPermissionState.status) {
        // If the camera permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            Text("Camera permission Granted")
        }
        is PermissionStatus.Denied -> {
            Column {
                val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
                    // If the user has denied the permission but the rationale can be shown,
                    // then gently explain why the app requires this permission
                    "The camera is important for this app. Please grant the permission."
                } else {
                    // If it's the first time the user lands on this feature, or the user
                    // doesn't want to be asked again for this permission, explain that the
                    // permission is required
                    "Camera permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}