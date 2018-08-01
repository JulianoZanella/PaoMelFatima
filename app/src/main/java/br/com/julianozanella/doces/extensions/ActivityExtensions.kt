package br.com.julianozanella.doces.extensions

import android.app.Activity
import android.widget.Toast

fun Activity.messageShort(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.messageLong(message: String){
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}