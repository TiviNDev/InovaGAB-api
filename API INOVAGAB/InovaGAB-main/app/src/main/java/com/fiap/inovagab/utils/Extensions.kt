package com.fiap.inovagab.utils

import android.content.Context
import android.widget.Toast
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Double.toBRL(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return format.format(this)
}

fun String?.toBrazilianDate(): String {
    if (this.isNullOrBlank()) {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }
    return this.take(10)
}