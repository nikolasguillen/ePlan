package com.cosmobile.eplan.core.util

import android.content.Context

sealed class UiText {
    data class DynamicString(val text: String) : UiText()
    data class StringResource(val resId: Int) : UiText()
    data class StringResourceWithArgs(val resId: Int, val args: List<Any>) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
            is StringResourceWithArgs -> {
                context.getString(
                    resId,
                    *args.map {
                        when (it) {
                            is UiText -> it.asString(context)
                            else -> it
                        }
                    }.toTypedArray()
                )
            }
        }
    }
}