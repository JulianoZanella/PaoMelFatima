package br.com.julianozanella.doces.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*


class MoneyTextWatcher(editText: EditText, locale: Locale? = null) : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText>?
    private val locale: Locale

    init {
        editTextWeakReference = WeakReference(editText)
        this.locale = locale ?: Locale.getDefault()
    }


    override fun afterTextChanged(editable: Editable?) {
        val editText = editTextWeakReference?.get() ?: return
        editText.removeTextChangedListener(this)

        try {
            val parsed = parseToBigDecimal(editable.toString(), locale)
            val formatted = NumberFormat.getCurrencyInstance(locale).format(parsed)

            editText.setText(formatted)
            editText.setSelection(formatted.length)
            editText.addTextChangedListener(this)
        } catch (e: Exception) {
        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    private fun parseToBigDecimal(value: String, locale: Locale): BigDecimal {
        val replaceable = String.format("[%s,.\\s]", NumberFormat.getCurrencyInstance(locale).currency.symbol)
        val cleanString = value.replace(replaceable.toRegex(), "")
        return BigDecimal(cleanString).setScale(
                2, BigDecimal.ROUND_FLOOR).divide(BigDecimal(100), BigDecimal.ROUND_FLOOR
        )
    }

}