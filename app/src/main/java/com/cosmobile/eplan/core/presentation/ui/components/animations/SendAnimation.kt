package com.cosmobile.eplan.core.presentation.ui.components.animations

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cosmobile.eplan.R

@Composable
fun SendAnimation() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.send))

    LottieAnimation(
        composition = composition,
        iterations = 1,
        speed = 2f,
        modifier = Modifier.size(size = 200.dp)
    )
}