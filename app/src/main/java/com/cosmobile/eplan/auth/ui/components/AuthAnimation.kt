package com.cosmobile.eplan.auth.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.rememberLottieDynamicProperties
import com.airbnb.lottie.compose.rememberLottieDynamicProperty
import com.cosmobile.eplan.R

@Composable
fun AuthAnimation(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.auth_animation))


    // Ai posteri: questo codice non funziona e non cambia il colore dell'animazione, capire perché
    val dynamicProperties = rememberLottieDynamicProperties(
        rememberLottieDynamicProperty(
            property = LottieProperty.COLOR_FILTER,
            value = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                MaterialTheme.colorScheme.primary.hashCode(),
                BlendModeCompat.SRC_ATOP
            ),
            keyPath = arrayOf(
                "OBJECTS 32",
                "OBJECTS 31",
                "OBJECTS 30",
                "OBJECTS 29",
                "OBJECTS 28",
                "OBJECTS 27",
                "OBJECTS 26",
                "OBJECTS 25",
                "OBJECTS 24",
                "OBJECTS 23",
                "OBJECTS 22",
                "OBJECTS 21",
                "OBJECTS 20",
                "OBJECTS 19",
                "OBJECTS 18",
                "OBJECTS 17"
            )
        )
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        LottieAnimation(
            composition = composition,
            iterations = 1,
            dynamicProperties = dynamicProperties,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}

@Preview
@Composable
private fun AuthAnimationPreview() {
    MaterialTheme {
        AuthAnimation(modifier = Modifier.fillMaxSize())
    }
}