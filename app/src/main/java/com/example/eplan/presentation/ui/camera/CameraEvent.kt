package com.example.eplan.presentation.ui.camera

sealed class CameraEvent {
    object SaveUriInCache: CameraEvent()
}