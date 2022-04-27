package com.example.eplan.presentation.ui.account

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eplan.R
import com.example.eplan.presentation.ui.components.CameraView
import com.example.eplan.presentation.util.TAG
import com.google.accompanist.permissions.*

const val name = "Nikolas"
// TODO prendere nome dal DB?

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel,
    onBackPressed: () -> Unit,
    toProfile: () -> Unit,
    toSettings: () -> Unit,
    toAppInfo: () -> Unit,
    logout: () -> Unit
) {
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    val show = remember { mutableStateOf(false) }

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

                if (show.value) {
                    CameraView(onImageCaptured = { uri, fromGallery ->
                        Log.d(TAG, "Image Uri Captured from Camera View")
//Todo : use the uri as needed

                    }, onError = { imageCaptureException ->
                    })
                }

                val rowModifier = Modifier.fillMaxWidth()
                val textModifier = Modifier.padding(end = 16.dp, top = 8.dp, bottom = 8.dp)
                val iconModifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)

                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = rowModifier
                        .clickable { toProfile() }) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = stringResource(R.string.profilo),
                        modifier = iconModifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .clickable {
                                when (cameraPermissionState.status) {
                                    PermissionStatus.Granted -> {
                                        show.value = !show.value
                                    }
                                    else -> {
                                        cameraPermissionState.launchPermissionRequest()
                                    }
                                }
                            }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
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
                    Spacer(modifier = Modifier.width(16.dp))
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
                    Spacer(modifier = Modifier.width(16.dp))
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