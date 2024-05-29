package com.cosmobile.eplan.core.presentation

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cosmobile.eplan.core.domain.use_cases.GetToken
import com.cosmobile.eplan.core.util.GENERIC_DEBUG_TAG
import com.cosmobile.eplan.core.util.USER_TOKEN
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

open class EplanViewModel : ViewModel() {

    protected var query = ""
    protected var userToken = USER_TOKEN
    var sending by mutableStateOf(false)
        protected set
    var retrieving by mutableStateOf(false)
        protected set

    /* Funzione per recuperare il token dal DB di Room */
    protected fun getToken(getToken: GetToken, onTokenRetrieved: () -> Unit = {}) {
        getToken.execute().onEach { dataState ->
            retrieving = dataState.loading

            dataState.data?.let { token ->
                userToken += token
                /*TODO teoricamente non serve più dato che l'id dell'intervento/appuntamento viene
                *  automaticamente salvato nel savedStateHandle grazie alla compose navigation.
                * Resta il problema che l'unica cosa che fa è rifare la chiamata web oppure ricreare
                * l'intervento da zero coi dati di partenza, quindi eventuali modifiche verranno sempre perse.
                * TODO ci riguarderò, però mi sembra un caso quasi limite*/

//                savedStateHandle.get<String>(STATE_KEY_INTERVENTION_ID)?.let { workActivityId ->
//                    onTriggerEvent(GetInterventionEvent(id = workActivityId))
//                }
                onTokenRetrieved()
            }

            dataState.error?.let { error ->
                Log.e(GENERIC_DEBUG_TAG, "getToken: $error")
                //TODO gestire errori
            }
        }.launchIn(viewModelScope)
    }
}