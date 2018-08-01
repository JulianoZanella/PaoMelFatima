package br.com.julianozanella.doces.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import br.com.julianozanella.doces.util.Converters
import java.io.Serializable
import java.util.*

@Entity(foreignKeys = [(ForeignKey(entity = Client::class, parentColumns = ["id"], childColumns = ["idClient"], onDelete = CASCADE))])
class Sale(@PrimaryKey(autoGenerate = true) var id: Int = 0,
           var idClient: Int = 0,
           var purchase: Float = 0.0F,
           var pay: Boolean = false,
           @TypeConverters(Converters::class) var payDate: Date? = null,
           @TypeConverters(Converters::class) var saleDate: Date = Date()): Serializable