package com.example.eplan.presentation.ui.camera

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eplan.interactors.GetToken
import com.example.eplan.interactors.camera.SaveProfilePicUri
import com.example.eplan.presentation.util.TAG
import com.example.eplan.presentation.util.USER_TOKEN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CameraViewModel
@Inject
constructor(
    private val getToken: GetToken,
    private val saveProfilePicUri: SaveProfilePicUri
) : ViewModel() {

    lateinit var imageUri: Uri
    private var userToken = USER_TOKEN

    init {
        getToken()
    }

    fun onTriggerEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.SaveUriInCache -> {
                savePicture()
            }
        }
    }

    private fun getToken() {
        getToken.execute().onEach { dataState ->
            dataState.data?.let { token ->
                userToken += token
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getToken: $error")
            }
        }.launchIn(viewModelScope)
    }

    private fun savePicture() {
        saveProfilePicUri.execute(imageUri = imageUri).onEach { dataState ->
            dataState.error?.let { error ->
                Log.e(TAG,"savePicture: $error")
            }
        }.launchIn(viewModelScope)
    }
}