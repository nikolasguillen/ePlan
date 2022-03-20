package com.example.eplan

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import com.example.eplan.domain.util.durationCalculator
import com.example.eplan.ui.items.MyAppTheme
import com.google.accompanist.insets.ProvideWindowInsets
import java.time.LocalTime


class MainActivity : AppCompatActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme() {
                MainScreen()
            }
        }
    }
}