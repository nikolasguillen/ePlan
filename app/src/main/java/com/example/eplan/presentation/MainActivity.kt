package com.example.eplan.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import com.example.eplan.presentation.ui.MainScreen
import com.example.eplan.presentation.ui.composables.MyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    /*companion object {
        private lateinit var instance: MainActivity

        private val database: EplanDatabase by lazy {
            EplanDatabase.buildDatabase(instance)
        }
    }*/

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                MainScreen()
            }
        }
    }
}