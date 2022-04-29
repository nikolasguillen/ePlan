package com.example.eplan.presentation.ui.camera

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import coil.compose.rememberAsyncImagePainter
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    onBackPressed: () -> Unit,
    onImageSelected: () -> Unit
) {

    var shouldShowPhoto by remember { mutableStateOf(false) }
    var shouldShowCamera by remember { mutableStateOf(true) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        if (shouldShowCamera) {
            CameraView(
                onImageCaptured = { uri, _ ->
                    viewModel.imageUri = uri
                    shouldShowCamera = false
                    shouldShowPhoto = true
                },
                onError = {
                    scope.launch {
                        it.localizedMessage?.let { it1 -> snackBarHostState.showSnackbar(it1) }
                    }
                }
            )
        }

        if (shouldShowPhoto) {
            CapturedImage(
                uri = viewModel.imageUri,
                savedImageUIAction = { action ->
                    when (action) {
                        is SavedImageUIAction.OnSelectClick -> {
                            onImageSelected()
                        }
                        is SavedImageUIAction.OnDiscardClick -> {
                            shouldShowPhoto = false
                            shouldShowCamera = true
                        }
                    }
                },
                onBackPressed = onBackPressed
            )
        }
    }
}

@Composable
private fun CapturedImage(
    uri: Uri,
    savedImageUIAction: (SavedImageUIAction) -> Unit,
    onBackPressed: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints() {

        }
        IconButton(
            onClick = { onBackPressed() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(vertical = MaterialTheme.spacing.medium)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "",
                modifier = Modifier.size(MaterialTheme.spacing.large)
            )
        }
        Image(
            painter = rememberAsyncImagePainter(model = uri),
            contentDescription = stringResource(
                R.string.immagine_scelta
            ),
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom
        ) {
            ImageControls(savedImageUIAction = savedImageUIAction)
        }
    }
}


@Composable
fun ImageControls(savedImageUIAction: (SavedImageUIAction) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.large
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        ImageControl(
            imageVector = Icons.Filled.RestartAlt,
            contentDescId = R.string.scatta_nuova_foto,
            modifier = Modifier.size(MaterialTheme.spacing.extraLarge),
            onClick = { savedImageUIAction(SavedImageUIAction.OnDiscardClick) }
        )

        ImageControl(
            imageVector = Icons.Filled.Done,
            contentDescId = R.string.conferma_foto,
            modifier = Modifier.size(MaterialTheme.spacing.extraLarge),
            onClick = { savedImageUIAction(SavedImageUIAction.OnSelectClick) }
        )
    }
}

@Composable
private fun ImageControl(
    imageVector: ImageVector,
    contentDescId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector,
            contentDescription = stringResource(id = contentDescId),
            modifier = modifier,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}