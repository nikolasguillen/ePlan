package com.example.eplan.presentation.ui.camera

sealed class SavedImageUIAction {
    object OnSelectClick : SavedImageUIAction()
    object OnDiscardClick : SavedImageUIAction()
}
