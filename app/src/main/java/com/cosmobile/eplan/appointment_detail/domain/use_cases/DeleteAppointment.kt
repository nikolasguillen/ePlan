package com.cosmobile.eplan.appointment_detail.domain.use_cases

import com.cosmobile.eplan.R
import com.cosmobile.eplan.appointment_detail.data.services.AppointmentDetailService
import com.cosmobile.eplan.core.domain.model.DataState
import com.cosmobile.eplan.core.util.UiText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class DeleteAppointment(private val service: AppointmentDetailService) {
    fun execute(token: String, id: String): Flow<DataState<Boolean>> = flow {

        emit(DataState.loading())

        val result = service.deleteAppointment(token, id)
        if (result.result.lowercase() == "ok" && result.message.lowercase() == "ok") {
            emit(DataState.success(true))
        } else {
            emit(
                DataState.error(
                    UiText.StringResourceWithArgs(
                        R.string.errore_server_with_args,
                        listOf(result.message)
                    )
                )
            )
        }
    }.catch { throwable ->
        emit(DataState.error(throwable.message?.let { UiText.DynamicString(it) }
            ?: UiText.StringResource(R.string.errore_sconosciuto)))
    }
}