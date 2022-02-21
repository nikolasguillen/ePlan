package com.example.eplan.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.fragment.app.Fragment
import com.example.eplan.R

class PaperFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PaperContent() {
        Scaffold(topBar = {TopBar()},
            bottomBar = {BottomNavBar()}) {
        }
    }

    @Composable
    private fun TopBar() {
        SmallTopAppBar(title = { Text(stringResource(id = R.string.app_name))})
    }

    @Composable
    private fun BottomNavBar() {
        NavigationBar(containerColor = colorResource(id = R.color.primaryLightColor),
        contentColor = colorResource(id = R.color.black),
        content = {})
    }
}