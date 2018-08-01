package br.com.julianozanella.doces.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import br.com.julianozanella.doces.model.Client
import br.com.julianozanella.doces.model.Sale
import br.com.julianozanella.doces.util.Converters


@Database(entities = [(Sale::class), (Client::class)], version = 5)
@TypeConverters(Converters::class)
abstract class SaleDatabase : RoomDatabase() {

    abstract fun salesDao(): SalesDao
    abstract fun clientDao(): ClientDao


    companion object {
        private var INSTANCE: SaleDatabase? = null

        fun getDatabase(context: Context): SaleDatabase {
            if (INSTANCE == null) {
                synchronized(SaleDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                SaleDatabase::class.java, "SaleDatabase")
                                .fallbackToDestructiveMigration()
                                .build()
                    }
                }
            }
            return INSTANCE as SaleDatabase
        }
    }
}