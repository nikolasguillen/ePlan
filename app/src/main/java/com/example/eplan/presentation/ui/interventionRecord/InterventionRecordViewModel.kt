package com.example.eplan.presentation.ui.interventionRecord

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

const val STATE_KEY_START = "intervention.state.start.key"
const val STATE_KEY_END = "intervention.state.end.key"
const val STATE_KEY_ISRECORDING = "intervention.state.isrecording.key"
const val BLANK_TIME = "--:--"

@HiltViewModel
class InterventionRecordViewModel
@Inject
constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val isRecording = mutableStateOf(false)
    private val isOver = mutableStateOf(false)
    val start = mutableStateOf(BLANK_TIME)
    val end = mutableStateOf(BLANK_TIME)

    init {
        savedStateHandle.get<Boolean>(STATE_KEY_ISRECORDING)?.let {
            isRecording.value = it
        }
        savedStateHandle.get<String>(STATE_KEY_START)?.let {
            start.value = it
        }
        savedStateHandle.get<String>(STATE_KEY_END)?.let {
            end.value = it
        }
    }

    fun isRecording(): Boolean {
        return isRecording.value
    }

    fun isOver(): Boolean {
        return isOver.value
    }

    fun setStart(time: LocalTime) {
        start.value = time.truncatedTo(ChronoUnit.MINUTES).toString()
        savedStateHandle.set(STATE_KEY_START, start.value)
        isRecording.value = true
        savedStateHandle.set(STATE_KEY_ISRECORDING, isRecording.value)
        isOver.value = false
    }

    fun setEnd(time: LocalTime) {
        end.value = time.truncatedTo(ChronoUnit.MINUTES).toString()
        savedStateHandle.set(STATE_KEY_END, end.value)
        isRecording.value = false
        savedStateHandle.set(STATE_KEY_ISRECORDING, isRecording.value)
        isOver.value = true
    }

    fun reset() {
        isRecording.value = false
        isOver.value = false
        start.value = BLANK_TIME
        end.value = BLANK_TIME
    }
}