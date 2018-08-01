package br.com.julianozanella.doces.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData

class MesAnoLiveData(private val ano: LiveData<Int>, private val mes: LiveData<Int>) : MediatorLiveData<Pair<Int, Int>>() {
    init {
        addSource(ano) { year -> value = Pair(year!!, ano.value!!) }
        addSource(mes) { month -> value = Pair(month!!, mes.value!!) }
    }
}