package com.example.eplan.presentation.ui.camera

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.camera.SaveProfilePicUri
import com.example.eplan.presentation.ui.EplanViewModel
import com.example.eplan.presentation.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CameraViewModel
@Inject
constructor(
    getToken: GetToken,
    private val saveProfilePicUri: SaveProfilePicUri
) : EplanViewModel() {

    lateinit var imageUri: Uri

    init {
        getToken(getToken = getToken)
    }

    fun onTriggerEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.SaveUriInCache -> {
                savePicture()
            }
        }
    }

    private fun savePicture() {
        saveProfilePicUri.execute(imageUri = imageUri).onEach { dataState ->
            dataState.error?.let { error ->
                Log.e(TAG,"savePicture: $error")
            }
        }.launchIn(viewModelScope)
    }
}