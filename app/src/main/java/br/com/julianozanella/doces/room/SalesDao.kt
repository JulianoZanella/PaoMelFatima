package br.com.julianozanella.doces.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import br.com.julianozanella.doces.model.Sale

@Dao
interface SalesDao {

    @Insert(onConflict = REPLACE)
    fun insert(sale: Sale)

    @Update
    fun update(sale: Sale)

    @Delete
    fun delete(sale: Sale)

    @Query("SELECT * FROM Sale")
    fun getSales(): LiveData<List<Sale>>

    @Query("SELECT * FROM Sale WHERE strftime('%Y',datetime(saleDate/1000, 'unixepoch')) = :arg0 AND strftime('%m', datetime(saleDate/1000, 'unixepoch')) = :arg1 ORDER BY datetime(saleDate/1000, 'unixepoch') DESC")
    fun getSales(arg0: String, arg1: String): LiveData<List<Sale>>

    @Query("SELECT IFNULL(SUM(purchase), 0) FROM Sale WHERE strftime('%Y',datetime(saleDate/1000, 'unixepoch')) = :arg0 AND strftime('%m', datetime(saleDate/1000, 'unixepoch')) = :arg1")
    fun getTotalValue(arg0: String, arg1: String): LiveData<Float>

    @Query("SELECT IFNULL(SUM(purchase), 0) FROM Sale WHERE pay = 1 AND strftime('%Y',datetime(saleDate/1000, 'unixepoch')) = :arg0 AND strftime('%m', datetime(saleDate/1000, 'unixepoch')) = :arg1")
    fun getReceivedValue(arg0: String, arg1: String): LiveData<Float>
}