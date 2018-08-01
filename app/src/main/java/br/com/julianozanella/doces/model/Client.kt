package br.com.julianozanella.doces.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
class Client(@PrimaryKey(autoGenerate = true) var id: Int = 0,
             var name: String = "",
             var phone: String = "",
             var idContact: Long? = null,
             @ColumnInfo(typeAffinity = ColumnInfo.BLOB) var photoArray: ByteArray? = null): Serializable