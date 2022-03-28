package com.example.eplan.presentation.ui.workActivity

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.eplan.domain.model.User
import com.example.eplan.domain.model.WorkActivity
import com.example.eplan.repository.WorkActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ActivityDetailViewModel
@Inject
constructor(
    private val repository: WorkActivityRepository,
    @ApplicationContext context: Context
) : ViewModel() {

    val workActivity: MutableState<WorkActivity?> = mutableStateOf(null)

    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    fun onTriggerEvent(event: ActivityDetailEvent) {

    }

    private suspend fun getActivityById(id: Int) {
        resetActivity()
        val result = repository.getActivityById( )
    }

    private fun resetActivity() {
        workActivity.value = null
    }

}