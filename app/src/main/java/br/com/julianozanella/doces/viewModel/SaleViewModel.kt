package br.com.julianozanella.doces.viewModel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import br.com.julianozanella.doces.model.Sale
import br.com.julianozanella.doces.room.SaleRepository
import java.util.*

class SaleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SaleRepository(application)
    private val salesByMonthYear: LiveData<List<Sale>>
    private val totalValueByMonthYear: LiveData<Float>
    private val paidValueByMonthYear: LiveData<Float>
    private val filterByMonthYear: MutableLiveData<FilterMonthYear> = MutableLiveData()
    private var m = 0
    private var y = 0
    val updateDate = MutableLiveData<Date>()

    init {
        salesByMonthYear = Transformations.switchMap(filterByMonthYear) { it ->
            repository.getSales(it.month, it.year)
        }

        totalValueByMonthYear = Transformations.switchMap(filterByMonthYear) { it ->
            repository.getPurchases(it.month, it.year)
        }

        paidValueByMonthYear = Transformations.switchMap(filterByMonthYear) { it ->
            repository.getPaidPurchases(it.month, it.year)
        }
    }

    fun insert(sale: Sale) {
        repository.insert(sale)
    }

    fun update(sale: Sale) {
        repository.update(sale)
    }

    fun delete(sale: Sale) {
        repository.delete(sale)
    }

    fun getSales() = repository.getAllSales()

    fun setYear(year: Int) {
        y = year
        setFilter()
    }

    fun setMonth(month: Int) {
        m = month
        setFilter()
    }

    fun getSalesByMonthYear() = salesByMonthYear

    fun getTotalValue() = totalValueByMonthYear

    fun getPaidValue() = paidValueByMonthYear

    private fun setFilter() {
        setFilter(y, m)
    }

    fun setFilter(year: Int, month: Int) {
        y = year
        m = month
        val update = FilterMonthYear(year, month)
        if (Objects.equals(filterByMonthYear.value, update)) {
            return
        }
        filterByMonthYear.value = update
    }

    companion object {
        class FilterMonthYear(val year: Int, val month: Int)
    }

}