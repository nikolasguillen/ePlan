package com.example.eplan.presentation.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.eplan.presentation.util.USER_TOKEN
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow


/** Classe astratta che i ViewModel delle pagine di dettaglio estendono,
 * usato per poter utilizzare un unico composable per le liste di interventi e appuntamenti
 */
abstract class WorkActivityDetailViewModel : ViewModel() {

    protected var userToken = USER_TOKEN
    protected var query = ""
    var retrieving by mutableStateOf(false)
        protected set
    var sending by mutableStateOf(false)
        protected set
    protected val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    abstract fun checkChanges(): Boolean

    protected abstract fun changeQuery(query: String)
}