package com.example.eplan.network.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.time.LocalDate
import java.time.LocalTime

/** Divide il dato da come ci arriva (data + ora in una singola stringa) in un Pair che le contiene separatamente **/
fun dateTimeParser(dateTime: String): Pair<LocalDate, LocalTime> {
    return Pair(
        first = LocalDate.parse(dateTime.split(" ")[0]),
        second = LocalTime.parse(dateTime.split(" ")[1])
    )
}

/** Compose mostra il tag HTML per l'andata a capo, quindi tocca levarlo manualmente*/
fun removeHtmlBreak(text: String): String {
    return text
        .replace(oldValue = "<br />", newValue = "")
        .replace(oldValue = "<br/>", newValue = "")
}

fun isConnectionAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}