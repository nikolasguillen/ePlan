package com.cosmobile.eplan.core.presentation.ui.components.animations

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder

@Composable
fun GifImage(
    modifier: Modifier = Modifier,
    imageID: Int,
    imageDescription: String
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context = context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    Image(
        painter = rememberAsyncImagePainter(model = imageID, imageLoader = imageLoader),
        contentDescription = imageDescription,
        modifier = modifier
    )
}