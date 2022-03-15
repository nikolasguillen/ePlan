package com.example.eplan.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class Person(val name: String, var isChecked: MutableState<Boolean> = mutableStateOf(false))