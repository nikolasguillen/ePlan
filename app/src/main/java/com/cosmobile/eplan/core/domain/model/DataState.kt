package com.cosmobile.eplan.core.domain.model

import com.cosmobile.eplan.core.util.UiText

data class DataState<out T>(
    val data: T? = null,
    val error: UiText? = null,
    val loading: Boolean = false
) {
    companion object {

        fun <T> success(
            data: T
        ): DataState<T> {
            return DataState(
                data = data
            )
        }

        fun <T> error(
            message: UiText
        ): DataState<T> {
            return DataState(
                error = message
            )
        }

        fun <T> loading(): DataState<T> = DataState(loading = true)
    }
}