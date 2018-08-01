package br.com.julianozanella.doces.room

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import br.com.julianozanella.doces.model.Client

@Dao
interface ClientDao {

    @Insert(onConflict = REPLACE)
    fun insert(client: Client)

    @Update
    fun update(client: Client)

    @Delete
    fun delete(client: Client)

    @Query("SELECT * FROM Client ORDER BY name asc")
    fun getClients(): LiveData<List<Client>>

    @Query("SELECT C.id, C.name, C.phone, C.photoArray, C.idContact FROM Client C INNER JOIN Sale V ON (V.idClient = C.ID) WHERE V.pay = 0 group by C.id ORDER BY V.id DESC")
    fun getDebtors(): LiveData<List<Client>>

    @Query("SELECT SUM(V.purchase) FROM Sale V WHERE V.idClient = :arg0")
    fun getShopping(arg0: Int): Float

    @Query("SELECT SUM(V.purchase) FROM Sale V WHERE V.idClient = :arg0 AND V.pay = 0 ORDER BY ID DESC")
    fun getCharge(arg0: Int): Float

    @Query("UPDATE Sale SET pay = 1 WHERE idClient = :arg0")
    fun payCharges(arg0: Int)

    @Query("SELECT name FROM Client WHERE id = :arg0")
    fun getName(arg0: Int): String
}