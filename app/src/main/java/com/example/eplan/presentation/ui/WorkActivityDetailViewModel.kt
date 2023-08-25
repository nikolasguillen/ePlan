package com.example.eplan.presentation.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


/* Classe astratta che i ViewModel delle pagine di dettaglio estendono,
 * usato per poter utilizzare un unico composable per le liste di interventi e appuntamenti
 */

abstract class WorkActivityDetailViewModel : EplanViewModel() {

    protected val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    var isConnectionAvailable by mutableStateOf(true)
        protected set

    abstract fun checkChanges(): Boolean

    protected abstract fun changeQuery(query: String)
}