package br.com.julianozanella.doces.room

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import br.com.julianozanella.doces.model.Client
import br.com.julianozanella.doces.model.Sale


class SaleRepository(application: Application) {
    private val salesDao: SalesDao
    private val clientDao: ClientDao
    private val allSales: LiveData<List<Sale>>
    private val allClients: LiveData<List<Client>>
    private val allDebtors: LiveData<List<Client>>

    init {
        val db = SaleDatabase.getDatabase(application)
        salesDao = db.salesDao()
        clientDao = db.clientDao()
        allSales = salesDao.getSales()
        allClients = clientDao.getClients()
        allDebtors = clientDao.getDebtors()
    }

    fun getAllSales(): LiveData<List<Sale>> {
        return allSales
    }

    fun getSales(mes: Int, ano: Int): LiveData<List<Sale>> {
        val month: String = if (mes < 10) "0$mes" else mes.toString()
        val year = "$ano"
        return salesDao.getSales(year, month)
    }

    fun getAllClients() = allClients

    fun getDebtors() = allDebtors

    fun getPaidPurchases(mes: Int, ano: Int): LiveData<Float> {
        val month: String = if (mes < 10) "0$mes" else mes.toString()
        val year = "$ano"
        return salesDao.getReceivedValue(year, month)
    }

    fun getPurchases(mes: Int, ano: Int): LiveData<Float> {
        val month: String = if (mes < 10) "0$mes" else mes.toString()
        val year = "$ano"
        return salesDao.getTotalValue(year, month)
    }

    fun insert(sale: Sale) {
        InsertAsyncTask(salesDao).execute(sale)
    }

    fun update(sale: Sale) {
        UpdateAsyncTask(salesDao).execute(sale)
    }

    fun delete(sale: Sale) {
        DeleteAsyncTask(salesDao).execute(sale)
    }

    fun insert(client: Client) {
        InsertActivityAsyncTask(clientDao).execute(client)
    }

    fun update(client: Client) {
        UpdateActivityAsyncTask(clientDao).execute(client)
    }

    fun delete(client: Client) {
        DeleteActivityAsyncTask(clientDao).execute(client)
    }

    fun getSales(id: Int): Float = CountTotalValuesAsyncTask(clientDao).execute(id).get()

    fun getCharge(id: Int): Float = CountDebtAsyncTask(clientDao).execute(id).get()

    fun payCharge(id: Int) {
        PaidDebtAsyncTask(clientDao).execute(id)
    }

    fun getNameClient(idClient: Int): String = GetNameAsyncTask(clientDao).execute(idClient).get()


    private class InsertAsyncTask internal constructor(private val mAsyncTaskDao: SalesDao) : AsyncTask<Sale, Void, Void>() {

        override fun doInBackground(vararg params: Sale): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class UpdateAsyncTask internal constructor(private val mAsyncTaskDao: SalesDao) : AsyncTask<Sale, Void, Void>() {

        override fun doInBackground(vararg params: Sale): Void? {
            mAsyncTaskDao.update(params[0])
            return null
        }
    }

    private class DeleteAsyncTask internal constructor(private val mAsyncTaskDao: SalesDao) : AsyncTask<Sale, Void, Void>() {

        override fun doInBackground(vararg params: Sale): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }

    private class InsertActivityAsyncTask internal constructor(private val mAsyncTaskDao: ClientDao) : AsyncTask<Client, Void, Void>() {

        override fun doInBackground(vararg params: Client): Void? {
            mAsyncTaskDao.insert(params[0])
            return null
        }
    }

    private class UpdateActivityAsyncTask internal constructor(private val mAsyncTaskDao: ClientDao) : AsyncTask<Client, Void, Void>() {

        override fun doInBackground(vararg params: Client): Void? {
            mAsyncTaskDao.update(params[0])
            return null
        }
    }

    private class DeleteActivityAsyncTask internal constructor(private val mAsyncTaskDao: ClientDao) : AsyncTask<Client, Void, Void>() {

        override fun doInBackground(vararg params: Client): Void? {
            mAsyncTaskDao.delete(params[0])
            return null
        }
    }


    private class CountTotalValuesAsyncTask internal constructor(private val mAsyncTaskDao: ClientDao) : AsyncTask<Int, Void, Float>() {
        override fun doInBackground(vararg p0: Int?): Float {
            return mAsyncTaskDao.getShopping(p0[0] ?: 0)
        }
    }


    private class CountDebtAsyncTask internal constructor(private val mAsyncTaskDao: ClientDao) : AsyncTask<Int, Void, Float>() {
        override fun doInBackground(vararg p0: Int?): Float {
            return mAsyncTaskDao.getCharge(p0[0] ?: 0)
        }

    }

    private class PaidDebtAsyncTask internal constructor(private val mAsyncTaskDao: ClientDao) : AsyncTask<Int, Void, Void>() {
        override fun doInBackground(vararg p0: Int?): Void? {
            mAsyncTaskDao.payCharges(p0[0] ?: 0)
            return null
        }

    }

    private class GetNameAsyncTask internal constructor(private val mAsyncTaskDao: ClientDao) : AsyncTask<Int, Void, String>() {
        override fun doInBackground(vararg p0: Int?): String {
            return mAsyncTaskDao.getName(p0[0] ?: 0)
        }

    }
}

