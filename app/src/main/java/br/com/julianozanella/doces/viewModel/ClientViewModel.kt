package br.com.julianozanella.doces.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import br.com.julianozanella.doces.model.Client
import br.com.julianozanella.doces.room.SaleRepository

class ClientViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SaleRepository(application)

    fun insert(client: Client) {
        repository.insert(client)
    }

    fun update(client: Client) {
        repository.update(client)
    }

    fun delete(client: Client) {
        repository.delete(client)
    }

    fun getClients() = repository.getAllClients()

    fun getDebtors() = repository.getDebtors()

    fun getCharge(id: Int): Float = repository.getCharge(id)

    fun payCharge(id: Int) = repository.payCharge(id)
}