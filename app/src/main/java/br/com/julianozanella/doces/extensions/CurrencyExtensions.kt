package br.com.julianozanella.doces.extensions

import android.app.Activity
import android.os.Build
import java.text.NumberFormat
import java.util.*

fun Activity.getPurchaseValue(currencyString: String): Float {
    val mLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0]
    } else {
        Locale("pt", "BR")
    }
    val valorNumber = NumberFormat.getCurrencyInstance(mLocale).parse(currencyString)
    return valorNumber.toFloat()
}

